package net.boardrank.boardgame;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Profile("dev")
@Component
public class PopulatorForTest implements ApplicationRunner {

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BoardgameRepository boardGameRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GameMatchService gameMatchService;

    @Autowired
    AccountService accountService;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Account a = accountService.addNewAccount("a@a.a","1","a");
        Account b = accountService.addNewAccount("b@b.b","1","b");
        Account c = accountService.addNewAccount("c@c.c","1","c");
        Account d = accountService.addNewAccount("d@d.d","1","d");

        this.accountService.makeFriend(a, b);
        this.accountService.makeFriend(a, c);

        Boardgame boardgame1 = new Boardgame();
        boardgame1.setName("boardgame1");
        boardGameRepository.save(boardgame1);
        Boardgame boardgame2 = new Boardgame();
        boardgame2.setName("boardgame2");
        boardGameRepository.save(boardgame2);

        this.gameMatchService.makeNewMatch("😀😀😀😀😀11111", boardgame1, Arrays.asList(a, b), a);
        this.gameMatchService.makeNewMatch("😀😀😀😀😀22222", boardgame1, Arrays.asList(a, b), a);

        accountService.requestFriend(d,a);
    }

}
