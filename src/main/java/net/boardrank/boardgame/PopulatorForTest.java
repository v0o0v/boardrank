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
                ,"https://lh3.googleusercontent.com/a-/AOh14GhOO7GhiYH7vKpCh5U3cwI37dNHsVecnw6yQwGrSA=s96-c");
        Account b = accountService.addNewAccount("b@b.b","b",null);
        Account c = accountService.addNewAccount("c@c.c","c",null);
        Account d = accountService.addNewAccount("d@d.d","d",null);

        this.accountService.makeFriend(mskim, b);
        this.accountService.makeFriend(mskim, c);

        Boardgame 테라포밍마스 = this.boardgameService.addBoardgame("테라포밍마스", mskim, false, null);
        Boardgame 격동 = this.boardgameService.addBoardgame("격동", mskim, true, 테라포밍마스);
        Boardgame 비너스 = this.boardgameService.addBoardgame("비너스", mskim, true, 테라포밍마스);
        Boardgame 마르코폴로 = this.boardgameService.addBoardgame("마르코폴로", mskim, false, null);

        GameMatch gameMatch = this.gameMatchService.makeNewMatch(테라포밍마스, Arrays.asList(mskim, b), mskim);
        gameMatch = this.gameMatchService.addExpansion(gameMatch, Arrays.asList(격동));

        gameMatch.getRankentries().forEach(rankEntry -> {
            rankEntry.setRank(1);
            rankEntry.setScore(10);
        });
        gameMatch.setStartedTime(LocalDateTime.now());
        gameMatch.setFinishedTime(LocalDateTime.now());
        gameMatch.setAcceptedTime(LocalDateTime.now());
        gameMatch.setBoardgameProvider(mskim);
        gameMatch.setRuleSupporter(mskim);
        gameMatch.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatch = gameMatchService.save(gameMatch);


        GameMatch gameMatch2 = this.gameMatchService.makeNewMatch(마르코폴로, Arrays.asList(mskim, b), mskim);

        accountService.requestFriend(d,mskim);
    }

}
