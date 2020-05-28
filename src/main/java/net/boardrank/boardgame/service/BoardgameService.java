package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.repository.jpa.BoardgameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardgameService {

    @Autowired
    BoardgameRepository boardGameRepository;

    @Transactional
    public List<Boardgame> getAllBoardgame(){
        return boardGameRepository.findAll();
    }

    @Transactional
    public boolean isExistAsName(String name) {
        return boardGameRepository.existsBoardgameByName(name);
    }

    @Transactional
    public Boardgame addBoardgame(String name, Account creator, boolean isExp, Boardgame base) {
        Boardgame boardgame = new Boardgame(name, isExp);
        boardgame.setCreator(creator);
        if(isExp) boardgame.setBase(base);
        return boardGameRepository.save(boardgame);
    }

    @Transactional
    public List<Boardgame> getAllBaseBoardgames(){
        return this.boardGameRepository.findAllByExpIs(false);
    }
}
