package net.boardrank.account.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import net.boardrank.account.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Account account = this.accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
//        return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));

        return User.withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();
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

    public boolean isExist(String email) {
        return this.accountRepository.findByEmail(email).isPresent();
    }

    public Account addAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.saveAndFlush(account);
    }

    public Page<Account> getAccounts(Pageable pageable) {

        return this.accountRepository.findAll(pageable);
    }
}
