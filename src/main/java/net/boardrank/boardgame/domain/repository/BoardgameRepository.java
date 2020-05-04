package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.Boardgame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardgameRepository extends JpaRepository<Boardgame,Long> {

}
