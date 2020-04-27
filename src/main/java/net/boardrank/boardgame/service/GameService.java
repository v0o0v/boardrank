package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.Game;
import net.boardrank.boardgame.domain.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    public List<Game> getGamesOfCurrentSessionAccount() {
        Account account = this.accountService.getCurrentAccount();

        List<Game> gamesByPaticiantAccountsContaining = this.gameRepository.findGamesByPaticiantAccountsContaining(account);
        return gamesByPaticiantAccountsContaining;
    }



}
