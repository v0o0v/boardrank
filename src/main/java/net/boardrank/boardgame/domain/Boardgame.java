package net.boardrank.boardgame.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    private boolean exp;

    @ManyToOne
    private Boardgame base;

    @OneToMany(mappedBy = "base", fetch = FetchType.EAGER)
    private Set<Boardgame> expansionSet = new HashSet<>();

    @Override
    public String toString() {
        return this.name;
    }

    public Boardgame(String name, boolean exp) {
        this.exp = exp;
        this.name = name;
    }
}
