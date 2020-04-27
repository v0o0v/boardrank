package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.Friend;
import net.boardrank.account.domain.repository.AccountRepository;
import net.boardrank.account.domain.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FriendRepository friendRepository;

    @Value("${net.boardrank.friend.max}")
    Integer maxFriendNum;

    @Transactional
    public void makeFriend(Account a, Account b) {

        if (a.getFriends().size() >= maxFriendNum || b.getFriends().size() >= maxFriendNum)
            throw new RuntimeException("Exceed Max Friend Number");

        Friend ab = new Friend();
        ab.setFriend(b);
        this.friendRepository.save(ab);
        a.addFriend(ab);
        this.accountRepository.save(a);

        Friend ba = new Friend();
        ba.setFriend(a);
        this.friendRepository.save(ba);
        b.addFriend(ba);
        this.accountRepository.save(b);
    }

}
