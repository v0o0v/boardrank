package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.repository.BoardgameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardgameService {

    @Autowired
    BoardgameRepository boardGameRepository;

    public List<Boardgame> getAllBoardgame(){
        return boardGameRepository.findAll();
    }

}
