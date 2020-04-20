package net.boardrank.Boardgame.service;

import net.boardrank.Boardgame.domain.Game;
import net.boardrank.Boardgame.domain.repository.GameRepository;
import net.boardrank.account.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    public List<Game> getGamesIncludeOf(Account account){
        return this.gameRepository.findGamesByPaticiantAccountsContaining(account);
    }
}
