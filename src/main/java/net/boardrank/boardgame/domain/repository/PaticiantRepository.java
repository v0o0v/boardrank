package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.Paticiant;
import net.boardrank.boardgame.domain.RankEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaticiantRepository extends JpaRepository<Paticiant,Long> {


}
