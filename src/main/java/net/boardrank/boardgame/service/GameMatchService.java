package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.dynamo.AccountMatchStatusRepository;
import net.boardrank.boardgame.domain.repository.dynamo.CommentRepository;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.jpa.RankEntryRepository;
import net.boardrank.boardgame.service.matchAcceptFilter.MatchAcceptFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GameMatchService {

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RankEntryRepository rankEntryRepository;

    @Autowired
    S3Service s3Service;

    @Autowired
    MatchAcceptFilterChain matchAcceptFilterChain;

    @Autowired
    AccountMatchStatusRepository accountMatchStatusRepository;

    @Transactional
    public GameMatch getGameMatch(Long id) {
        return this.gameMatchRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public List<GameMatch> getGamesOfCurrentSessionAccount() {
        return this.gameMatchRepository.findGameMatchesByRankentriesAccount(this.accountService.getCurrentAccount());
    }

    @Transactional
    public List<GameMatch> getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus gameMatchStatus) {
        return this.gameMatchRepository.findGameMatchesByRankentriesAccountAndGameMatchStatusIs(
                this.accountService.getCurrentAccount()
                , GameMatchStatus.resultAccepted);
    }

    @Transactional
    public GameMatch makeNewMatch(Boardgame bg, List<Account> parties, Account createdAccount) {
        GameMatch match = new GameMatch(bg, createdAccount);
        parties.stream().forEach(account -> {
            RankEntry rankEntry = this.rankEntryRepository.save(new RankEntry(account, match));
            match.getRankentries().add(rankEntry);
        });
        return this.gameMatchRepository.save(match);
    }

    @Transactional
    public GameMatch addExpansion(GameMatch gameMatch, List<Boardgame> exps) {
        exps.forEach(boardgame -> {
            gameMatch.getExpansions().add(boardgame);
        });
        return this.gameMatchRepository.save(gameMatch);
    }

    @Transactional
    public List<GameMatch> getInprogressMatches(Account account) {
        return this.gameMatchRepository
                .findGameMatchesByRankentriesAccountAndGameMatchStatusIsNot(account, GameMatchStatus.resultAccepted);
    }

    @Transactional
    public GameMatch setGameMatchStatus(GameMatch gameMatch, GameMatchStatus gameMatchStatus) {
        gameMatch.setGameMatchStatus(gameMatchStatus);
        if (gameMatchStatus == GameMatchStatus.finished) {
            this.sendNoticeToAcceptMatchResult(gameMatch);
        }
        return this.gameMatchRepository.save(gameMatch);
    }

    @Transactional
    public void sendNoticeToAcceptMatchResult(GameMatch gameMatch) {
        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {
                    noticeService.noticeToAcceptMatch(gameMatch, gameMatch.getCreatedMember(), rankEntry.getAccount());
                });
    }

    @Transactional
    public GameMatch setStartTime(GameMatch gameMatch, LocalDateTime dateTime) {
        gameMatch.setStartedTime(dateTime);
        return this.gameMatchRepository.save(gameMatch);
    }

    @Transactional
    public GameMatch setFinishTime(GameMatch gameMatch, LocalDateTime dateTime) {
        gameMatch.setFinishedTime(dateTime);
        return this.gameMatchRepository.save(gameMatch);
    }

    @Transactional
    public GameMatch save(GameMatch gameMatch) {
        return this.gameMatchRepository.save(gameMatch);
    }

    @Transactional
    public void handleMatchAccept(Notice notice, NoticeResponse response, Account me) {
        GameMatch gameMatch = notice.getGameMatch();
        gameMatch.applyResultAcceptance(me, response.equals(NoticeResponse.Accept) ? true : false);

        if (gameMatch.isAccetableOfMatchResult()) {
            this.applyMatchResult(gameMatch);
            noticeService.finishAllOnMatchAccepted(notice.getGameMatch(), notice.getNoticeType());
            return;
        }
        noticeService.finish(notice);
    }

    //게임 결과를 반영하고 Match를 종료시킨다.
    @Transactional
    public void applyMatchResult(GameMatch gameMatch) {
        if (!gameMatch.getGameMatchStatus().equals(GameMatchStatus.finished))
            return;

        matchAcceptFilterChain.doFilterChain(gameMatch);

        gameMatch.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatch.setAcceptedTime(LocalDateTime.now());
        this.gameMatchRepository.save(gameMatch);
    }

    @Transactional
    public void removeMatch(GameMatch gameMatch) {

        //삭제 전에 관련 notice 삭제
        noticeService.removeNoticeOf(gameMatch);

        this.gameMatchRepository.delete(gameMatch);
    }

    public void addComment(Long gameMatchId, Account currentAccount, String value) {
        Comment comment = new Comment(gameMatchId, currentAccount.getId(), currentAccount.getName(), value);
        this.commentRepository.save(comment);
    }

    public List<Comment> getCommentsByMatchId(Long gameMatchId) {
        return this.commentRepository.findAllByGameMatchIdOrderByCreatedAtAsc(gameMatchId);
    }

    public AccountService getAccountService() {
        return this.accountService;
    }

    @Transactional
    public GameMatch setBoardgameProvider(GameMatch match, Account provider) {
        match.setBoardgameProvider(provider);
        return this.save(match);
    }

    @Transactional
    public GameMatch setRuleSupporter(GameMatch match, Account ruler) {
        match.setRuleSupporter(ruler);
        return this.save(match);
    }

    public String uploadImage(InputStream inputStream, String mime, Account uploader) throws Exception {
        String filename = UUID.randomUUID().toString();
        switch (mime) {
            case "image/jpeg":
                filename = filename + ".jpg";
                break;
            case "image/png":
                filename = filename + ".png";
                break;
            case "image/gif":
                filename = filename + ".gif";
                break;
            default:
                throw new RuntimeException("지원하지 않는 파일 형식입니다.");
        }
        return this.s3Service.upload(filename, inputStream);
    }

    @Transactional
    public GameMatch addImage(GameMatch gameMatch, String filename, Account uploader) {
        ImageURL imageURL = new ImageURL(uploader, filename, gameMatch);
        gameMatch.getImages().add(imageURL);
        return this.save(gameMatch);
    }

    @Transactional
    public GameMatch deleteImage(GameMatch gameMatch, String filename) {

        try {
            s3Service.delete(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameMatch.deleteImageURL(filename);
        return this.save(gameMatch);
    }

    public String getURLAsCloundFront(String filename) {
        return s3Service.getURLAsCloundFront(filename);
    }

    public List<GameMatch> getLast5Match(Account account) {
        return this.gameMatchRepository.findTop5GameMatchesByRankentriesAccountAndAcceptedTimeIsNotNullOrderByAcceptedTimeDesc(account);
    }

    public void addAccountMatchStatus(Long accountId, String accountName, Long boardgameId, String attribute, String value) {
        AccountMatchStatus accountMatchStatus = new AccountMatchStatus(accountId, accountName, boardgameId, attribute, value);
        this.accountMatchStatusRepository.save(accountMatchStatus);
    }
}
