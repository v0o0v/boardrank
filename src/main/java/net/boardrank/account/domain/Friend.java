package net.boardrank.account.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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

    @Override
    public String toString() {
        return friend.toString();
    }
}
