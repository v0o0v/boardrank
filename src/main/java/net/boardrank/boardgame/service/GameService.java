package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.boardgame.domain.Game;
import net.boardrank.boardgame.domain.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<Game> getGamesOfCurrentSessionAccount() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findByEmail(details.getUsername()).orElseThrow(RuntimeException::new);

        List<Game> gamesByPaticiantAccountsContaining = this.gameRepository.findGamesByPaticiantAccountsContaining(account);
        return gamesByPaticiantAccountsContaining;
    }

}
