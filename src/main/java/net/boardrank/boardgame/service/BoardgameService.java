package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.repository.BoardgameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardgameService {

    @Autowired
    BoardgameRepository boardGameRepository;

    public List<Boardgame> getAllBoardgame(){
        return boardGameRepository.findAll();
    }

    public boolean isExistAsName(String name) {
        return boardGameRepository.existsBoardgameByName(name);
    }

    @Transactional
    public void addBoardgame(String name, Account creator) {
        Boardgame boardgame = new Boardgame();
        boardgame.setName(name);
        boardgame.setCreator(creator);
        boardGameRepository.save(boardgame);
    }
}
