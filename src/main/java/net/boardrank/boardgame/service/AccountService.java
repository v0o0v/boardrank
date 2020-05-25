package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.AccountRole;
import net.boardrank.boardgame.domain.Friend;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.FriendRepository;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeResponse;
import net.boardrank.boardgame.domain.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    NoticeService noticeService;

    @Autowired
    FriendRepository friendRepository;

    @Value("${net.boardrank.friend.max}")
    Integer maxFriendNum;

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        Account account = this.accountRepository.findByEmail(username).orElseThrow(EntityNotFoundException::new);
        return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
    }

    public Account getAccountByUsername(String username) throws UsernameNotFoundException {
        return this.accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }

    public void saveAccount(Account account) {
        this.accountRepository.saveAndFlush(account);
    }

    public Account changePassword(String username, String password) throws UsernameNotFoundException {
        Account account = this.accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        account.setPassword(this.passwordEncoder.encode(password));
        return this.accountRepository.saveAndFlush(account);
    }

    public boolean isExistEmail(String email) {
        return this.accountRepository.findByEmail(email).isPresent();
    }

    public boolean isExistName(String name) {
        return this.accountRepository.findByName(name).isPresent();
    }

    @Transactional
    public Account addAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.saveAndFlush(account);
    }

    public Page<Account> getAccounts(Pageable pageable) {

        return this.accountRepository.findAll(pageable);
    }

    public List<Account> getAllByEmailContaining(String txt){
        return this.accountRepository.getAllByEmailContaining(txt);
    }

    public Account getCurrentAccount() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findByEmail(details.getUsername()).orElseThrow(RuntimeException::new);
    }

    public Account addNewAccount(String email, String pw, String name) {
        if(isExistEmail(email))
            throw new RuntimeException("이미 있는 email입니다.");

        if(isExistName(name))
            throw new RuntimeException("이미 있는 name입니다.");

        Account account = new Account(email,pw,name);

        return this.addAccount(account);
    }

    public Set<Account> getAccountsContainsName(String value) {
        return this.accountRepository.findAllByNameContains(value);
    }

    public void requestFriend(Account fromAccount, Account toAccount) {
        this.noticeService.noticeToMakeFriend(fromAccount, toAccount);
    }

    public boolean isProgressMakeFriend(Account from, Account to) {
        return noticeService.isExistNotice(NoticeType.friendRequest, from, to);
    }

    @Transactional
    public void handleRequestFriend(Notice notice, NoticeResponse response) {
        switch (response){
            case Accept:
                makeFriend(notice.getFrom(), notice.getTo());
                break;
            case Deny:
                break;
        }
        noticeService.finish(notice);
    }

    @Transactional
    public void makeFriend(Account a, Account b) {

        if (a.getFriends().size() >= maxFriendNum || b.getFriends().size() >= maxFriendNum)
            throw new RuntimeException("Exceed Max Friend Number");

        LocalDateTime now = LocalDateTime.now();

        Friend ab = new Friend(a, b, now);
        a.addFriend(ab);
        this.accountRepository.save(a);

        Friend ba = new Friend(b, a, now);
        b.addFriend(ba);
        this.accountRepository.save(b);
    }

    @Transactional
    public void removeFriend(Account me, Friend friend) {
        me.getFriends().remove(friend);
        accountRepository.save(me);
        friendRepository.delete(friend);
    }
}
