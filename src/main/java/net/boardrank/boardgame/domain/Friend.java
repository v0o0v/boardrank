package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Friend {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Account friend;

    @ManyToOne
    private Account owner;

    private LocalDateTime dday;

    public Friend(Account owner, Account friend, LocalDateTime dday) {
        this.owner = owner;
        this.friend = friend;
        this.dday = dday;
    }

    @Override
    public String toString() {
        return friend.toString();
    }
}
