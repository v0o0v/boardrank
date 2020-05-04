package net.boardrank.boardgame;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.BoardGameRepository;
import net.boardrank.boardgame.domain.repository.MatchRepository;
import net.boardrank.boardgame.domain.repository.RankEntryRepository;
import net.boardrank.boardgame.service.FriendService;
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
    MatchRepository matchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BoardGameRepository boardGameRepository;

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

        Account a = Account.builder().email("a").name("a").password(passwordEncoder.encode("1")).roles(roles).build();
        a.setBoardPoint(1150);
        Account b = Account.builder().email("b").name("b").password(passwordEncoder.encode("1")).roles(roles).build();
        b.setBoardPoint(930);
        Account c = Account.builder().email("c").name("c").password(passwordEncoder.encode("1")).roles(roles).build();
        c.setBoardPoint(1000);
        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        this.friendService.makeFriend(a,b);
        this.friendService.makeFriend(a,c);


        Match g1 = new Match();
        matchRepository.save(g1);

        BoardGame boardGame1 = new BoardGame();
        boardGame1.setName("boardgame1");
        boardGameRepository.save(boardGame1);
        g1.setBoardGame(boardGame1);

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

        g1.setMatchStatus(MatchStatus.init);
        g1.setCreatedTime(LocalDateTime.now().minusHours(2));
        g1.setStartedTime(LocalDateTime.now().minusHours(1));
        g1.setFinishedTime(LocalDateTime.now());
        g1.setMatchTitle("a vs b");
        g1.setChatId("dsfdfdf");

        matchRepository.saveAndFlush(g1);

        // ========================================

        Match g2 = new Match();

        BoardGame boardGame2 = new BoardGame();
        boardGame2.setName("boardgame2");
        boardGameRepository.save(boardGame2);
        g2.setBoardGame(boardGame2);

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

        g2.setMatchStatus(MatchStatus.resultAccepted);
        g2.setCreatedTime(LocalDateTime.now().minusHours(42));
        g2.setStartedTime(LocalDateTime.now().minusMinutes(128));
        g2.setFinishedTime(LocalDateTime.now().minusMinutes(777));
        g2.setMatchTitle("a vs c");
        g2.setChatId("4654g");

        matchRepository.save(g2);
    }

}
