package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class RankEntry {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account")
    private Account account;

    private Integer score=0;

    private Integer rank=0;

    public RankEntry(Account account) {
        this.account = account;
    }
}
