package net.bgmgr.Boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bgmgr.account.domain.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class RankEntry {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private Account account;

    private Double score;

    private Integer rank;
}
