package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    private GameMatch gameMatch;

    @ManyToOne
    private Account account;

    private Integer score=0;

    private Integer rank=1;

    private ResultAcceptStatus resultAcceptStatus = ResultAcceptStatus.None;

    public RankEntry(Account account, GameMatch gameMatch) {
        this.account = account;
        this.gameMatch = gameMatch;
    }

    @Override
    public String toString() {
        return "RankEntry{" +
                "id=" + id +
                ", gameMatch=" + gameMatch +
                ", account=" + account +
                '}';
    }
}
