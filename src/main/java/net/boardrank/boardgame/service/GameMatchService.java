package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.domain.repository.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.RankEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return this.gameMatchRepository.save(gameMatch);
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
}
