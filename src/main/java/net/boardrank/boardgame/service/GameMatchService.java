package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.dynamo.CommentRepository;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.jpa.RankEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Value("${net.boardrank.point.win}")
    Integer winPoint;

    @Value("${net.boardrank.point.lose}")
    Integer losePoint;

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
    public GameMatch makeNewMatch(String name, Boardgame bg, List<Account> parties, Account createdAccount) {
        GameMatch match = new GameMatch(name, bg, createdAccount);
        parties.stream().forEach(account -> {
            RankEntry rankEntry = this.rankEntryRepository.save(new RankEntry(account, match));
            match.getRankentries().add(rankEntry);
        });
        return this.gameMatchRepository.save(match);
    }

    @Transactional
    public GameMatch addExpansion(GameMatch gameMatch, List<Boardgame> exps){
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

        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {
                    int bp = gameMatch.getNumOfSmallerThanMe(rankEntry) * winPoint
                            - gameMatch.getNumOfGreaterThanMe(rankEntry) * losePoint;
                    rankEntry.getAccount().addBP(bp);
                    accountService.saveAccount(rankEntry.getAccount());
                });
        gameMatch.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatch.setAcceptedTime(LocalDateTime.now());
        this.gameMatchRepository.save(gameMatch);
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
}
