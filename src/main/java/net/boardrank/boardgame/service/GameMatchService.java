package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.domain.Paticiant;
import net.boardrank.boardgame.domain.repository.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.PaticiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    PaticiantRepository paticiantRepository;

    public List<GameMatch> getGamesOfCurrentSessionAccount() {
        Account account = this.accountService.getCurrentAccount();

        List<GameMatch> gamesByPaticiantAccountsContaining = this.gameMatchRepository.findGameMatchesByPaticiantAccountsContaining(account);
        return gamesByPaticiantAccountsContaining;
    }

    @Transactional
    public GameMatch addMatch(String name, Boardgame bg, List<Account> parties, Account createdAccount) {
        Paticiant paticiant = new Paticiant(parties);
        paticiant = this.paticiantRepository.saveAndFlush(paticiant);
        GameMatch match = new GameMatch(name, bg, paticiant, createdAccount);

        return this.gameMatchRepository.save(match);
    }

    public List<GameMatch> getPlayingMatches(Account account) {
        return this.gameMatchRepository
                .findGameMatchesByPaticiantAccountsContainingAndGameMatchStatusIsNot(account, GameMatchStatus.resultAccepted);
    }
}
