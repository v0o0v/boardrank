package net.boardrank.Boardgame.domain.repository;

import net.boardrank.Boardgame.domain.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameRepository extends JpaRepository<BoardGame,Long> {

}
