package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
