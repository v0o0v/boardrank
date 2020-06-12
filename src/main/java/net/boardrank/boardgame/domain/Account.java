package net.boardrank.boardgame.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String pictureURL;

    private Integer boardPoint;

    private Integer angelPoint;

    private String locale;


    @Enumerated(EnumType.ORDINAL)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<AccountRole> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "owner")
    private Set<Friend> friends = new HashSet<>();

    @Builder
    public Account(String email, Set<AccountRole> roles, String name, String picture, String locale) {
        this.email = email;
        this.roles = roles;
        this.name = name;
        this.boardPoint = 1000;
        this.angelPoint = 0;
        this.pictureURL = picture;
        this.locale = locale;
    }

    public Account(String email, String name, String picture, String locale) {
        this(email, new HashSet<>(Arrays.asList(AccountRole.USER)), name, picture, locale);
    }

    public void addFriend(Friend friend) {
        this.friends.add(friend);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public List<Account> getFriendsAsAccounType() {
        return this.getFriends().stream()
                .map(Friend::getFriend)
                .collect(Collectors.toList())
                ;
    }

    public List<Account> getMeAndFriends() {
        List all = new ArrayList();
        all.add(this);
        all.addAll(this.friends.stream()
                .map(Friend::getFriend)
                .collect(Collectors.toList())
        );
        return all;
    }

    public void addBP(int bp) {
        this.boardPoint += bp;
    }

    public void addAP(int ap) {
        this.angelPoint += ap;
    }
}
