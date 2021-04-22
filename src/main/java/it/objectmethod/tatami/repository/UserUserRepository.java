package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.UserUser;

@Repository
public interface UserUserRepository extends JpaRepository<UserUser, Long> {

	@Query(nativeQuery = true, name = "SELECT * \n"
		+ "FROM user_user \n"
		+ "WHERE user_1_id = :userId AND relationship = :relationShip")
	List<UserUser> findByUser1(@Param("userId") Long user1Id, @Param("relationShip") String relationShip);

	@Query(nativeQuery = true, name = "SELECT * \n"
		+ "FROM user_user \n"
		+ "WHERE user_1_id = :user1Id AND user_2_id = :user2Id AND relationship = :relationShip")
	UserUser findByUser1AndUser2AndRelation(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id,
		@Param("relationShip") String relationShip);
}
