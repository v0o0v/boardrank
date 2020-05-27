package net.boardrank.boardgame.domain.repository.jpa;

import net.boardrank.boardgame.domain.Boardgame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardgameRepository extends JpaRepository<Boardgame,Long> {
    public boolean existsBoardgameByName(String name);

    public List<Boardgame> findAllByExpIs(boolean isExp);
}
