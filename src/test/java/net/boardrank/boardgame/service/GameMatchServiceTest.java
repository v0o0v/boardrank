package net.boardrank.boardgame.service;

import net.boardrank.IntegrationTest;
import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.jpa.NoticeRepository;
import net.boardrank.boardgame.service.matchAcceptFilter.MatchAcceptFilterChain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class GameMatchServiceTest extends IntegrationTest {

    @Autowired
    GameMatchService gameMatchService;

    @Autowired
    AccountService accountService;

    @Autowired
    BoardgameService boardgameService;

    @Autowired
    BoardgameRepository boardgameRepository;

    @Autowired
    GameMatchRepository gameMatchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @MockBean
    MatchAcceptFilterChain matchAcceptFilterChain;


    //테스트를 위한 기본 데이터 생성
    @Before
    public void setUp() throws Exception {
        Account a = accountService.addNewAccount("a@a.a", "a", "picURL", "ko");
        Account b = accountService.addNewAccount("b@b.b", "b", null, "ko");
        Account c = accountService.addNewAccount("c@c.c", "c", null, null);

        this.accountService.makeFriend(a, b);
        this.accountService.makeFriend(a, c);


        Boardgame bg1 = this.boardgameService.addBoardgame("bg1", a, false, null);
        Boardgame bg1_1 = this.boardgameService.addBoardgame("bg1_1", a, true, bg1);

        GameMatch gameMatch1 = this.gameMatchService.makeNewMatch(bg1, Arrays.asList(a, b), a);
        gameMatch1 = this.gameMatchService.addExpansion(gameMatch1, Arrays.asList(bg1_1));

        gameMatch1.getRankentries().forEach(rankEntry -> {
            rankEntry.setRank(1);
            rankEntry.setScore(10);
        });
        gameMatch1.setStartedTime(LocalDateTime.now());
        gameMatch1.setFinishedTime(LocalDateTime.now());
        gameMatch1.setAcceptedTime(LocalDateTime.now());
        gameMatch1.setBoardgameProvider(a);
        gameMatch1.setRuleSupporter(a);
        gameMatch1.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatch1 = gameMatchService.save(gameMatch1);

        initOAuth2Accout(a.getEmail());
    }

    // 각 테스트마다 의존성을 없애기 위해 테스트 이후 before에서 생성한 데이터 삭제
    @After
    public void tearDown() throws Exception {
        gameMatchRepository.deleteAll();
        noticeRepository.deleteAll();
        boardgameRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void getGameMatch() {

        Boardgame bg1 = this.boardgameService.getAllBaseBoardgames().get(0);
        Account account1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account account2 = this.accountService.getAccountsContainsName("b").iterator().next();

        GameMatch gameMatch = this.gameMatchService.makeNewMatch(bg1, Arrays.asList(account1, account2), account1);
        GameMatch gameMatch1 = this.gameMatchService.getGameMatch(gameMatch.getId());

        assertThat(gameMatch).isEqualTo(gameMatch1);
    }

    @Test
    public void getGamesOfCurrentSessionAccountOnGameStatus() {

        List<GameMatch> gamesOfCurrentSessionAccountOnGameStatus = this.gameMatchService.getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus.resultAccepted);
        assertThat(gamesOfCurrentSessionAccountOnGameStatus.size()).isEqualTo(1);

        gamesOfCurrentSessionAccountOnGameStatus = this.gameMatchService.getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus.finished);
        assertThat(gamesOfCurrentSessionAccountOnGameStatus.size()).isEqualTo(0);

    }

    @Test
    public void makeNewMatch() {
        int size = gameMatchRepository.findAll().size();

        Boardgame b1 = this.boardgameService.getAllBaseBoardgames().get(0);
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();

        GameMatch gameMatch = this.gameMatchService.makeNewMatch(b1, Arrays.asList(a1, a2), a1);

        assertThat(gameMatch.getBoardGame()).isEqualTo(b1);
        assertThat(gameMatch.getCreatedMember()).isEqualTo(a1);
        assertThat(gameMatchRepository.findAll().size()).isEqualTo(size + 1);
    }

    @Test
    public void addExpansion() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();

        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();

        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        assertThat(gameMatch.getExpansions().size()).isEqualTo(0);

        gameMatchService.addExpansion(gameMatch, Arrays.asList(exp));
        assertThat(gameMatch.getExpansions().size()).isEqualTo(1);
    }

    @Test
    public void getInprogressMatches() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();

        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        List<GameMatch> inprogressMatches = gameMatchService.getInprogressMatches(a1);
        assertThat(inprogressMatches.contains(gameMatch)).isTrue();

        gameMatch.setGameMatchStatus(GameMatchStatus.resultAccepted);
        gameMatchService.save(gameMatch);
        assertThat(gameMatchService.getInprogressMatches(a1).contains(gameMatch)).isFalse();

    }

    @Test
    public void setGameMatchStatus() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();

        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        assertThat(gameMatch.getGameMatchStatus()).isEqualTo(GameMatchStatus.init);
        assertThat(noticeService.getNoticeListOfToAccount(a1).size()).isZero();

        gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.finished);
        assertThat(gameMatch.getGameMatchStatus()).isEqualTo(GameMatchStatus.finished);
        assertThat(noticeService.getNoticeListOfToAccount(a1).size()).isOne();
    }

    @Test
    public void sendNoticeToAcceptMatchResult() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        assertThat(gameMatch.getGameMatchStatus()).isEqualTo(GameMatchStatus.init);
        assertThat(noticeService.getNoticeListOfToAccount(a1).size()).isZero();

        gameMatchService.sendNoticeToAcceptMatchResult(gameMatch);
        assertThat(noticeService.getNoticeListOfToAccount(a1).size()).isOne();
    }

    @Test
    public void setStartTime() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        LocalDateTime now = LocalDateTime.now();
        gameMatchService.setStartTime(gameMatch, now);
        //gameMatch.getStartedTime()에서 9시간 더해서 반환하기 때문
        assertThat(gameMatch.getStartedTime()).isEqualTo(now.plusHours(9));
    }

    @Test
    public void setFinishTime() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        LocalDateTime now = LocalDateTime.now();
        gameMatchService.setFinishTime(gameMatch, now);
        //gameMatch.getStartedTime()에서 9시간 더해서 반환하기 때문
        assertThat(gameMatch.getFinishedTime()).isEqualTo(now.plusHours(9));
    }

    @Test
    public void handleMatchAccept() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.finished);

        Notice notice = noticeService.getNoticeListOfToAccount(a1).get(0);
        gameMatchService.handleMatchAccept(notice, NoticeResponse.Accept, a1);

        //2명중 1명이 accept했기 때문에 match는 accepted된다.
        assertThat(gameMatch.getGameMatchStatus()).isEqualTo(GameMatchStatus.resultAccepted);
        //해당 게임이 accepted 되었기 때문에 관련된 notice는 전부 삭제된다.
        assertThat(noticeService.getNoticeListOfToAccount(a1).size()).isZero();
        assertThat(noticeService.getNoticeListOfToAccount(a2).size()).isZero();
    }

    @Test
    public void applyMatchResult() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.finished);
        gameMatchService.applyMatchResult(gameMatch);
        gameMatch = this.gameMatchService.getGameMatch(gameMatch.getId());

        assertThat(gameMatch.getGameMatchStatus()).isEqualTo(GameMatchStatus.resultAccepted);
    }

    @Test
    public void removeMatch() {
        List<GameMatch> all = this.gameMatchRepository.findAll();
        int numOfMatches = all.size();
        this.gameMatchService.removeMatch(all.get(0));
        assertThat(numOfMatches-1).isEqualTo(this.gameMatchRepository.findAll().size());
    }

    @Test
    public void setBoardgameProvider() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        assertThat(gameMatch.getBoardgameProvider()).isNull();

        gameMatch = gameMatchService.setBoardgameProvider(gameMatch,a1);
        assertThat(gameMatch.getBoardgameProvider()).isEqualTo(a1);
    }

    @Test
    public void setRuleSupporter() {
        Boardgame exp = this.boardgameRepository.findAllByExpIs(true).get(0);
        Boardgame base = exp.getBase();
        Account a1 = this.accountService.getAccountsContainsName("a").iterator().next();
        Account a2 = this.accountService.getAccountsContainsName("b").iterator().next();
        GameMatch gameMatch = this.gameMatchService.makeNewMatch(base, Arrays.asList(a1, a2), a1);
        assertThat(gameMatch.getRuleSupporter()).isNull();

        gameMatch = gameMatchService.setRuleSupporter(gameMatch,a1);
        assertThat(gameMatch.getRuleSupporter()).isEqualTo(a1);
    }
}