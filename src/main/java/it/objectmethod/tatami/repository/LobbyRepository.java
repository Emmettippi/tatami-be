package it.objectmethod.tatami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.Lobby;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

}
