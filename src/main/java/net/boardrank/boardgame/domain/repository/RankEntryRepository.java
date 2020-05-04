package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.RankEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankEntryRepository extends JpaRepository<RankEntry,Long> {


}
