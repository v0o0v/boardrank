package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.BoardGame;
import net.boardrank.boardgame.domain.Game;
import net.boardrank.boardgame.domain.repository.BoardGameRepository;
import net.boardrank.boardgame.domain.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardGameService {

    @Autowired
    BoardGameRepository boardGameRepository;

    public List<BoardGame> getAllBoardgame(){
        return boardGameRepository.findAll();
    }

}
