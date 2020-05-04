package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.Match;
import net.boardrank.boardgame.domain.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    public List<Match> getGamesOfCurrentSessionAccount() {
        Account account = this.accountService.getCurrentAccount();

        List<Match> gamesByPaticiantAccountsContaining = this.matchRepository.findGamesByPaticiantAccountsContaining(account);
        return gamesByPaticiantAccountsContaining;
    }



}
