package net.boardrank.boardgame.domain.repository.jpa;

import net.boardrank.boardgame.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
