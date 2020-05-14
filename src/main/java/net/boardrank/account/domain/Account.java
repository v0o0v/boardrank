package net.boardrank.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private Integer boardPoint;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<AccountRole> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Friend> friends = new HashSet<>();

    @Builder
    public Account(String email, String password, Set<AccountRole> roles, String name) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.name = name;
        this.boardPoint = 1000;
    }

    public Account(String email, String pw, String name) {
        this(email, pw, new HashSet<>(Arrays.asList(AccountRole.USER)), name);
    }

    public void addFriend(Friend friend){
        this.friends.add(friend);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public List<Account> getFriendsAsAccounType(){
        return this.getFriends().stream()
                .map(Friend::getFriend)
                .collect(Collectors.toList())
        ;
    }

}
