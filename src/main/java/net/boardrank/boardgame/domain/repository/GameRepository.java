package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.Game;
import net.boardrank.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Long> {

    public List<Game> findGamesByPaticiantAccountsContaining(Account account);

}