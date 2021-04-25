package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.UserUser;
import it.objectmethod.tatami.entity.enums.UserRelation;

@Repository
public interface UserUserRepository extends JpaRepository<UserUser, Long> {

	List<UserUser> findByUser1_IdAndRelationship(Long user1Id, UserRelation relationShip);

	UserUser findByUser1_IdAndUser2_IdAndRelationship(Long user1Id, Long user2Id, UserRelation relationShip);

	List<UserUser> findByUser1_IdAndUser2_Id(Long user1Id, Long user2Id);

	List<UserUser> findByUser1_Id(Long id);

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM user_user WHERE user_1_id = :userId OR user_2_id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
}
