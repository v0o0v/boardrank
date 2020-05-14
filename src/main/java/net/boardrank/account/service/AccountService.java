package net.boardrank.account.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import net.boardrank.account.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void addNewAccount(String email, String pw, String name) {
        if(isExistEmail(email))
            throw new RuntimeException("이미 있는 email입니다.");

        if(isExistName(name))
            throw new RuntimeException("이미 있는 name입니다.");

        Account account = new Account(email,pw,name);

        this.addAccount(account);
    }

    public Set<Account> getAccountsContainsName(String value) {
        return this.accountRepository.findAllByNameContains(value);
    }
}
