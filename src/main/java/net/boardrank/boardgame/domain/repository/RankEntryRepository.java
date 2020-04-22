package net.boardrank.boardgame.domain.repository;

import net.boardrank.account.domain.Account;
import net.boardrank.boardgame.domain.Game;
import net.boardrank.boardgame.domain.RankEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankEntryRepository extends JpaRepository<RankEntry,Long> {


}
