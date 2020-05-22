package net.boardrank.boardgame.domain.repository.jpa;

import net.boardrank.boardgame.domain.RankEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankEntryRepository extends JpaRepository<RankEntry,Long> {


}
