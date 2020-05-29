package net.boardrank.boardgame;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
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
    GameMatchService gameMatchService;

    @Autowired
    AccountService accountService;

    @Autowired
    BoardgameService boardgameService;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Account a = accountService.addNewAccount("a@a.a","1","a");
        Account b = accountService.addNewAccount("b@b.b","1","b");
        Account c = accountService.addNewAccount("c@c.c","1","c");
        Account d = accountService.addNewAccount("d@d.d","1","d");

        this.accountService.makeFriend(a, b);
        this.accountService.makeFriend(a, c);

        Boardgame 테라포밍마스 = this.boardgameService.addBoardgame("테라포밍마스", a, false, null);
        Boardgame 격동 = this.boardgameService.addBoardgame("격동", a, true, 테라포밍마스);
        Boardgame 비너스 = this.boardgameService.addBoardgame("비너스", a, true, 테라포밍마스);
        Boardgame 마르코폴로 = this.boardgameService.addBoardgame("마르코폴로", a, false, null);

        GameMatch gameMatch1 = this.gameMatchService.makeNewMatch("😀😀😀😀😀11111", 테라포밍마스, Arrays.asList(a, b), a);
        gameMatch1 = this.gameMatchService.addExpansion(gameMatch1, Arrays.asList(격동));

        GameMatch gameMatch2 = this.gameMatchService.makeNewMatch("😀😀😀😀😀22222", 마르코폴로, Arrays.asList(a, b), a);

        accountService.requestFriend(d,a);
    }

}
