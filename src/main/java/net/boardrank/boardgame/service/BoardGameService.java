package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.BoardGame;
import net.boardrank.boardgame.domain.repository.BoardGameRepository;
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
