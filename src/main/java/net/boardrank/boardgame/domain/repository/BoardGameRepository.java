package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameRepository extends JpaRepository<BoardGame,Long> {

}
