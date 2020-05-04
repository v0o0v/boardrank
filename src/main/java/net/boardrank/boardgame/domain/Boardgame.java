package net.boardrank.boardgame.domain;

import lombok.*;
import net.boardrank.account.domain.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Boardgame {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Account creator;

    @Override
    public String toString() {
        return this.name;
    }
}
