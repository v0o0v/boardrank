package net.boardrank.boardgame;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.RankEntryRepository;
import net.boardrank.boardgame.service.FriendService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class PopulatorForTest implements ApplicationRunner {

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BoardgameRepository boardGameRepository;

    @Autowired
    RankEntryRepository rankEntryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FriendService friendService;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.USER);

        Account a = Account.builder().email("a").name("aaaaaaa").password(passwordEncoder.encode("1")).roles(roles).build();
        a.setBoardPoint(1150);
        Account b = Account.builder().email("b").name("bbbbbbbbb").password(passwordEncoder.encode("1")).roles(roles).build();
        b.setBoardPoint(930);
        Account c = Account.builder().email("c").name("v0o0v").password(passwordEncoder.encode("1")).roles(roles).build();
        c.setBoardPoint(1000);
        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        this.friendService.makeFriend(a,b);
        this.friendService.makeFriend(a,c);

        Boardgame boardgame2 = new Boardgame();
        boardgame2.setName("boardgame111111");
        boardGameRepository.save(boardgame2);

        GameMatch g1 = new GameMatch();
        gameMatchRepository.save(g1);

        Boardgame boardgame1 = new Boardgame();
        boardgame1.setName("boardgame2222222");
        boardGameRepository.save(boardgame1);
        g1.setBoardGame(boardgame1);

        Paticiant p1 = new Paticiant();
        Set<Account> s1 = new HashSet();
        s1.add(a);
        s1.add(b);
        p1.setAccounts(s1);
        g1.setPaticiant(p1);

        g1.setCreatedMember(a);

        ScoreBoard scoreBoard1 = new ScoreBoard();
        Set<RankEntry> rankEntrySet1 = new HashSet();

        RankEntry entry1 = new RankEntry();
        entry1.setAccount(a);
        entry1.setScore(100d);
        entry1.setRank(1);
        entry1 = this.rankEntryRepository.save(entry1);
        rankEntrySet1.add(entry1);

        RankEntry entry2 = new RankEntry();
        entry2.setAccount(b);
        entry2.setScore(80d);
        entry2.setRank(2);
        entry2 = this.rankEntryRepository.save(entry2);
        rankEntrySet1.add(entry2);

        scoreBoard1.setRankEntrySet(rankEntrySet1);
        g1.setScoreBoard(scoreBoard1);

        g1.setGameMatchStatus(GameMatchStatus.init);
        g1.setCreatedTime(LocalDateTime.now().minusHours(2));
        g1.setStartedTime(LocalDateTime.now().minusHours(1));
        g1.setFinishedTime(LocalDateTime.now());
        g1.setMatchTitle("a vs b");
        g1.setChatId("dsfdfdf");

        gameMatchRepository.saveAndFlush(g1);

        // ========================================

        for(int i=0;i<10;i++)
            makeNewMatch(a, c, boardgame2, RandomString.make(10));
    }

    private void makeNewMatch(Account a, Account c, Boardgame boardgame, String name) {
        GameMatch g2 = new GameMatch();

        g2.setBoardGame(boardgame);

        Paticiant p2 = new Paticiant();
        Set<Account> s2 = new HashSet();
        s2.add(a);
        s2.add(c);
        p2.setAccounts(s2);
        g2.setPaticiant(p2);

        g2.setCreatedMember(a);

        ScoreBoard scoreBoard2 = new ScoreBoard();
        Set<RankEntry> res2 = new HashSet();
        RankEntry entry3 = new RankEntry();
        entry3.setAccount(a);
        entry3.setScore(20d);
        entry3.setRank(1);
        entry3 = this.rankEntryRepository.save(entry3);
        res2.add(entry3);
        RankEntry entry4 = new RankEntry();
        entry4.setAccount(c);
        entry4.setScore(20d);
        entry4.setRank(1);
        entry4 = this.rankEntryRepository.save(entry4);
        res2.add(entry4);
        scoreBoard2.setRankEntrySet(res2);
        g2.setScoreBoard(scoreBoard2);

        g2.setGameMatchStatus(GameMatchStatus.finished);
        g2.setCreatedTime(LocalDateTime.now().minusHours(42));
        g2.setStartedTime(LocalDateTime.now().minusMinutes(128));
        g2.setFinishedTime(LocalDateTime.now().minusMinutes(777));
        g2.setMatchTitle(name);
        g2.setChatId("4654g");

        gameMatchRepository.save(g2);
    }

}
