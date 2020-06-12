package net.boardrank.boardgame;

import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.jpa.RankEntryRepository;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Profile("dev")
@Component
public class PopulatorForTest implements ApplicationRunner {

    @Autowired
    GameMatchService gameMatchService;

    @Autowired
    AccountService accountService;

    @Autowired
    BoardgameService boardgameService;

    @Autowired
    RankEntryRepository rankEntryRepository;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Account mskim = accountService.addNewAccount("v0o0v2@gmail.com","v0o0v"
                ,"https://lh3.googleusercontent.com/a-/AOh14GhLBU5mmkfJrHa6gCQXLd-pOxIaB4F9KMtgE8V5-w=s96-c", "ko");
        Account boardrank = accountService.addNewAccount("root.boardrank@gmail.com","root.boardrank",null,"ko");
        Account c = accountService.addNewAccount("c@c.c","c",null, null);
        Account d = accountService.addNewAccount("d@d.d","d",null, null);

        this.accountService.makeFriend(mskim, boardrank);
        this.accountService.makeFriend(mskim, c);

        Boardgame 테라포밍마스 = this.boardgameService.addBoardgame("테라포밍마스", mskim, false, null);
        Boardgame 격동 = this.boardgameService.addBoardgame("격동", mskim, true, 테라포밍마스);
        Boardgame 비너스 = this.boardgameService.addBoardgame("비너스", mskim, true, 테라포밍마스);
        Boardgame 마르코폴로 = this.boardgameService.addBoardgame("마르코폴로", mskim, false, null);

        GameMatch gameMatch1 = this.gameMatchService.makeNewMatch(테라포밍마스, Arrays.asList(mskim, boardrank), mskim);
        gameMatch1 = this.gameMatchService.addExpansion(gameMatch1, Arrays.asList(격동));

        gameMatch1.getRankentries().forEach(rankEntry -> {
            rankEntry.setRank(1);
            rankEntry.setScore(10);
        });
        gameMatch1.setStartedTime(LocalDateTime.now());
        gameMatch1.setFinishedTime(LocalDateTime.now());
        gameMatch1.setAcceptedTime(LocalDateTime.now());
        gameMatch1.setBoardgameProvider(mskim);
        gameMatch1.setRuleSupporter(mskim);
        gameMatch1.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatch1 = gameMatchService.save(gameMatch1);


        GameMatch gameMatch2 = this.gameMatchService.makeNewMatch(마르코폴로, Arrays.asList(mskim, boardrank), mskim);

        accountService.requestFriend(d,mskim);
    }

}
