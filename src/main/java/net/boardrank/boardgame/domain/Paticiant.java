package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Paticiant {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Account> accounts;

    public Paticiant(List<Account> parties) {
        this.accounts = new HashSet<>(parties);
    }
}
