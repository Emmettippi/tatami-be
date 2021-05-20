package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.Lobby;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM lobby WHERE closed = FALSE LIMIT :offset, :size")
	List<Lobby> findByClosedFalse(@Param("offset") Long offset, @Param("size") Integer size);
}
