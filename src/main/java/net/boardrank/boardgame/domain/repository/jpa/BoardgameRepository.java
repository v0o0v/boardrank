package net.boardrank.boardgame.domain.repository.jpa;

import net.boardrank.boardgame.domain.Boardgame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardgameRepository extends JpaRepository<Boardgame,Long> {
    public boolean existsBoardgameByName(String name);
}
