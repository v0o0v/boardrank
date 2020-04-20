package net.boardrank.backend.service;

import net.boardrank.Boardgame.domain.BoardGame;
import net.boardrank.Boardgame.domain.Game;
import net.boardrank.Boardgame.domain.repository.BoardGameRepository;
import net.boardrank.Boardgame.domain.repository.GameRepository;
import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import net.boardrank.account.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class PopulatorForTest {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BoardGameRepository boardGameRepository;


    @PostConstruct
    @Transactional
    public void populateTestData() {

        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.USER);

        Account a = Account.builder().email("a").password("1").roles(roles).build();
        Account b = Account.builder().email("b").password("1").roles(roles).build();
        Account c = Account.builder().email("c").password("1").roles(roles).build();
        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        BoardGame boardGame1 = new BoardGame();
        boardGame1.setName("boardgame1");
        boardGameRepository.save(boardGame1);

        Game g1 = new Game();
        g1.setBoardGame(boardGame1);




    }
}
