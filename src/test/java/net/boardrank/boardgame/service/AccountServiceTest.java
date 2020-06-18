package net.boardrank.boardgame.service;

import net.boardrank.IntegrationTest;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeResponse;
import net.boardrank.boardgame.domain.NoticeType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountServiceTest extends IntegrationTest {

    @Test
    public void saveAccount() {
        int size = accountRepository.findAll().size();
        Account account = new Account("noname@a.a", "name", null, null);
        accountService.saveAccount(account);
        assertThat(size + 1).isEqualTo(accountRepository.findAll().size());
    }

    @Test
    public void isExistEmail() {
        String email = "noname@a.a";
        assertThat(accountService.isExistEmail(email)).isFalse();

        Account account = new Account(email, "name", null, null);
        accountService.saveAccount(account);
        assertThat(accountService.isExistEmail(email)).isTrue();
    }

    @Test
    public void isExistName() {
        String name = "name1";
        assertThat(accountService.isExistName(name)).isFalse();

        Account account = new Account("Dfdsf", name, null, null);
        accountService.saveAccount(account);
        assertThat(accountService.isExistName(name)).isTrue();
    }

    @Test
    public void getAccount() {
        Account account = new Account("noname@a.a", "name", null, null);
        Account account1 = accountService.saveAccount(account);
        assertThat(accountService.getAccount(account1.getId())).isEqualTo(account1);
    }

    @Test(expected = RuntimeException.class)
    public void 없는계정일경우exception발생() {
        assertThat(accountService.getAccount(-1L)).isNull();
    }

    @Test
    public void getCurrentAccount() {
        String email = "abc@a.a";
        initOAuth2Accout(email);
        assertThat(accountService.getCurrentAccount().getEmail()).isEqualTo(email);
    }

    @Test
    public void addNewAccount() {
        String email = "aa.a.a";
        String name = "name";
        Account account = accountService.addNewAccount(email, name, null, null);
        assertThat(account.getEmail()).isEqualTo(email);
    }

    @Test
    public void getAccountsContainsName() {
        int size = accountService.getAccountsContainsName("a").size();
        String email = "aaa.a.a";
        String name = "name";
        Account account = accountService.addNewAccount(email, name, null, null);
        assertThat(accountService.getAccountsContainsName("a").size()).isEqualTo(size + 1);
    }

    @Test
    public void requestFriend() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);

        assertThat(noticeService.getNoticeListOfToAccount(b).size()).isZero();
        accountService.requestFriend(a, b);
        assertThat(noticeService.getNoticeListOfToAccount(b).size()).isOne();
        assertThat(noticeService.getNoticeListOfToAccount(b).get(0).getNoticeType()).isEqualTo(NoticeType.friendRequest);
    }

    @Test(expected = RuntimeException.class)
    public void requestFriend하고_또하면exception() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);

        accountService.requestFriend(a, b);
        accountService.requestFriend(a, b);
    }

    @Test
    public void isProgressMakeFriend() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);

        assertThat(accountService.isProgressMakeFriend(a, b)).isFalse();
        accountService.requestFriend(a, b);
        assertThat(accountService.isProgressMakeFriend(a, b)).isTrue();
    }

    @Test
    public void handleRequestFriend() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);
        accountService.requestFriend(a, b);
        Notice notice = noticeService.getNoticeListOfToAccount(b).get(0);

        assertThat(a.isFriend(b)).isFalse();
        accountService.handleRequestFriend(notice, NoticeResponse.Accept);
        assertThat(a.isFriend(b)).isTrue();
    }

    @Test
    public void makeFriend() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);

        assertThat(a.isFriend(b)).isFalse();
        accountService.makeFriend(a, b);
        assertThat(a.isFriend(b)).isTrue();
    }

    @Test
    public void removeFriend() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);

        accountService.makeFriend(a, b);
        assertThat(a.isFriend(b)).isTrue();
        accountService.removeFriend(a, a.getFriends().iterator().next());
        assertThat(a.isFriend(b)).isFalse();
    }

    @Test
    public void changeName() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        assertThat(a.getName()).isEqualTo("a");
        accountService.changeName(a, "b");
        assertThat(a.getName()).isEqualTo("b");
    }

    @Test
    public void getFriendStatus() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        Account b = accountService.addNewAccount("b", "b", null, null);
        assertThat(accountService.getFriendStatus(a, a)).isEqualTo(FriendStatus.Me);
        assertThat(accountService.getFriendStatus(a, b)).isEqualTo(FriendStatus.NotFriend);
        accountService.requestFriend(a, b);
        assertThat(accountService.getFriendStatus(a, b)).isEqualTo(FriendStatus.FriendRequested);

        Notice notice = noticeService.getNoticeListOfToAccount(b).get(0);
        accountService.handleRequestFriend(notice, NoticeResponse.Accept);
        assertThat(accountService.getFriendStatus(a, b)).isEqualTo(FriendStatus.Friend);
    }

    @Test
    public void changeOneLine() {
        Account a = accountService.addNewAccount("a", "a", null, null);
        a = accountService.changeOnelineIntroduce(a, "aaa");
        assertThat(a.getOnelineIntroduce()).isEqualTo("aaa");
    }
}