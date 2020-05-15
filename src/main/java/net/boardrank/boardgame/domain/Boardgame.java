package net.boardrank.boardgame.domain;

import lombok.*;

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
