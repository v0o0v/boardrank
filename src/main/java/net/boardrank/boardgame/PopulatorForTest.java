package net.boardrank.boardgame;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.RankEntryRepository;
import net.boardrank.boardgame.service.FriendService;
import net.boardrank.boardgame.service.GameMatchService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Autowired
    GameMatchService gameMatchService;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.USER);

        Account a = Account.builder().email("a").name("aaaaaaa").password(passwordEncoder.encode("1")).roles(roles).build();
        a.setBoardPoint(1150);
        accountRepository.save(a);
        Account b = Account.builder().email("b@b.com").name("bbbbbbbbb").password(passwordEncoder.encode("1")).roles(roles).build();
        b.setBoardPoint(930);
        accountRepository.save(b);
        Account c = Account.builder().email("c").name("v0o0v").password(passwordEncoder.encode("1")).roles(roles).build();
        c.setBoardPoint(1000);
        accountRepository.save(c);
        Account d = Account.builder().email("d").name(RandomString.make(15)).password(passwordEncoder.encode("1")).roles(roles).build();
        d.setBoardPoint(1000);
        accountRepository.save(d);
        Account e = Account.builder().email("e").name(RandomString.make(15)).password(passwordEncoder.encode("1")).roles(roles).build();
        d.setBoardPoint(1000);
        accountRepository.save(e);
        Account f = Account.builder().email("f").name(RandomString.make(15)).password(passwordEncoder.encode("1")).roles(roles).build();
        d.setBoardPoint(1000);
        accountRepository.save(f);
        Account g = Account.builder().email("g").name(RandomString.make(15)).password(passwordEncoder.encode("1")).roles(roles).build();
        d.setBoardPoint(1000);
        accountRepository.save(g);
        Account h = Account.builder().email("h").name(RandomString.make(15)).password(passwordEncoder.encode("1")).roles(roles).build();
        d.setBoardPoint(1000);
        accountRepository.save(h);

        this.friendService.makeFriend(a,b);
        this.friendService.makeFriend(a,c);
        this.friendService.makeFriend(a,d);
        this.friendService.makeFriend(a,e);
        this.friendService.makeFriend(a,f);
        this.friendService.makeFriend(a,g);
        this.friendService.makeFriend(a,h);

        Boardgame boardgame1 = new Boardgame();
        boardgame1.setName("boardgame2222222");
        boardGameRepository.save(boardgame1);
        Boardgame boardgame2 = new Boardgame();
        boardgame2.setName("boardgame111111");
        boardGameRepository.save(boardgame2);

        this.gameMatchService.makeNewMatch("😀😀😀😀😀", boardgame1, Arrays.asList(a,b,c,d,e,f,g,h),a);

//        GameMatch g1 = new GameMatch();
//        gameMatchRepository.save(g1);
//
//        g1.setBoardGame(boardgame1);
//
//        Paticiant p1 = new Paticiant();
//        Set<Account> s1 = new HashSet();
//        s1.add(a);
//        s1.add(b);
//        s1.add(c);
//        s1.add(d);
//        s1.add(e);
//        s1.add(f);
//        s1.add(g);
//        s1.add(h);
//        p1.setAccounts(s1);
//        g1.setPaticiant(p1);
//
//        g1.setCreatedMember(a);
//
//        ScoreBoard scoreBoard1 = new ScoreBoard();
//        Set<RankEntry> rankEntrySet1 = new HashSet();
//
//        RankEntry entry1 = new RankEntry();
//        entry1.setAccount(a);
//        entry1.setScore(100d);
//        entry1.setRank(1);
//        entry1 = this.rankEntryRepository.save(entry1);
//        rankEntrySet1.add(entry1);
//
//        RankEntry entry2 = new RankEntry();
//        entry2.setAccount(b);
//        entry2.setScore(80d);
//        entry2.setRank(2);
//        entry2 = this.rankEntryRepository.save(entry2);
//        rankEntrySet1.add(entry2);
//
//        scoreBoard1.setRankEntrySet(rankEntrySet1);
//        g1.setScoreBoard(scoreBoard1);
//
//        g1.setGameMatchStatus(GameMatchStatus.init);
//        g1.setCreatedTime(LocalDateTime.now().minusHours(2));
//        g1.setStartedTime(LocalDateTime.now().minusHours(1));
//        g1.setFinishedTime(LocalDateTime.now());
//        g1.setMatchTitle("a vs b");
//
//        gameMatchRepository.saveAndFlush(g1);

        // ========================================

//        makeNewMatch(a, b, boardgame1, RandomString.make(10), GameMatchStatus.init);
//        makeNewMatch(a, b, boardgame1, RandomString.make(10), GameMatchStatus.proceeding);
//        makeNewMatch(a, c, boardgame2, RandomString.make(10), GameMatchStatus.finished);
//        makeNewMatch(a, c, boardgame2, RandomString.make(10), GameMatchStatus.resultAccepted);
    }

    private void makeNewMatch(Account a, Account c, Boardgame boardgame, String name, GameMatchStatus gameMatchStatus) {
//        GameMatch g2 = new GameMatch();
//
//        g2.setBoardGame(boardgame);
//
//        Paticiant p2 = new Paticiant();
//        Set<Account> s2 = new HashSet();
//        s2.add(a);
//        s2.add(c);
//        p2.setAccounts(s2);
//        g2.setPaticiant(p2);
//
//        g2.setCreatedMember(a);
//
//        ScoreBoard scoreBoard2 = new ScoreBoard();
//        Set<RankEntry> res2 = new HashSet();
//        RankEntry entry3 = new RankEntry();
//        entry3.setAccount(a);
//        entry3.setScore(20d);
//        entry3.setRank(1);
//        entry3 = this.rankEntryRepository.save(entry3);
//        res2.add(entry3);
//        RankEntry entry4 = new RankEntry();
//        entry4.setAccount(c);
//        entry4.setScore(20d);
//        entry4.setRank(1);
//        entry4 = this.rankEntryRepository.save(entry4);
//        res2.add(entry4);
//        scoreBoard2.setRankEntrySet(res2);
//        g2.setScoreBoard(scoreBoard2);
//
//        g2.setGameMatchStatus(gameMatchStatus);
//        g2.setCreatedTime(LocalDateTime.now().minusHours(42));
//        g2.setStartedTime(LocalDateTime.now().minusMinutes(128));
//        g2.setMatchTitle(name);
//        g2.setPlace("somewhere");
//
//        gameMatchRepository.save(g2);
    }

}
