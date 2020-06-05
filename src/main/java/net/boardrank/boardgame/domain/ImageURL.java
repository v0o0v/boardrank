package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class ImageURL {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Account owner;

    @ManyToOne
    private GameMatch gameMatch;

    private String url;

    private LocalDateTime createdTime = LocalDateTime.now();

    public ImageURL(Account owner, String url, GameMatch gameMatch) {
        this.owner = owner;
        this.url = url;
        this.gameMatch = gameMatch;
    }
}
