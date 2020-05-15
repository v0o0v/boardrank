package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.AccountRepository;
import net.boardrank.boardgame.domain.repository.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.RankEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameMatchService {

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    RankEntryRepository rankEntryRepository;

    @Autowired
    NoticeService noticeService;

    @Value("${net.boardrank.point.win}")
    Integer winPoint;

    @Value("${net.boardrank.point.lose}")
    Integer losePoint;

    public List<GameMatch> getGamesOfCurrentSessionAccount() {
        return this.gameMatchRepository.findGameMatchesByRankentriesAccount(this.accountService.getCurrentAccount());
    }

    public List<GameMatch> getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus gameMatchStatus) {
        return this.gameMatchRepository.findGameMatchesByRankentriesAccountAndGameMatchStatusIs(
                this.accountService.getCurrentAccount()
                , GameMatchStatus.resultAccepted);
    }

    @Transactional
    public GameMatch makeNewMatch(String name, Boardgame bg, List<Account> parties, Account createdAccount) {
        GameMatch match = this.gameMatchRepository.save(new GameMatch(name, bg, createdAccount));
        Set<RankEntry> rankEntries = new HashSet<>();
        parties.stream().forEach(account -> {
            RankEntry rankEntry = new RankEntry(account);
            rankEntry = rankEntryRepository.save(rankEntry);
            rankEntries.add(rankEntry);
        });
        match.setRankentries(rankEntries);
        return this.gameMatchRepository.save(match);
    }

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
            noticeService.finishAllOnMatchAccepted(notice.getGameMatch(),notice.getNoticeType());
            return;
        }
        noticeService.finish(notice);
    }

    //게임 결과를 반영하고 Match를 종료시킨다.
    @Transactional
    public void applyMatchResult(GameMatch gameMatch) {
        if(!gameMatch.getGameMatchStatus().equals(GameMatchStatus.finished))
            return;

        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {
                    int bp = gameMatch.getNumOfSmallerThanMe(rankEntry)*winPoint
                            - gameMatch.getNumOfGreaterThanMe(rankEntry)*losePoint;
                    rankEntry.getAccount().addBP(bp);
                    accountService.saveAccount(rankEntry.getAccount());
                });
        gameMatch.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatch.setAcceptedTime(LocalDateTime.now());
        this.save(gameMatch);
    }
}
