package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(nativeQuery = true, value = "SELECT MD5(:pswd)")
	String md5Password(@Param("pswd") String password);

	User findByUsername(String username);

	User findByEmailIgnoreCase(String email);

	User findByUsernameAndPassword(String username, String password);

	List<User> findByNicknameContains(String nickname);

	@Query(nativeQuery = true, value = "SELECT u.* \n"
		+ "FROM user_info u \n"
		+ "  JOIN user_user uu ON u.id = uu.user_2_id \n"
		+ "WHERE uu.relationship = :relationShip \n"
		+ "  AND uu.user_1_id = :userId")
	List<User> findByUserIdAndRelation(@Param("userId") Long user1Id, @Param("relationShip") String relationShip);

	@Query(nativeQuery = true, value = "SELECT u.* \n"
		+ "FROM user_info u \n"
		+ "  JOIN user_user uu ON u.id = uu.user_2_id \n"
		+ "WHERE uu.relationship = :relationShip \n"
		+ "  AND u.id = :userId")
	List<User> findByUserId2AndRelation(@Param("userId") Long user2Id, @Param("relationShip") String relationShip);

	@Query(nativeQuery = true, value = "SELECT u.* \n"
		+ "FROM user_info u \n"
		+ "WHERE u.user_status <> 'OFFLINE' \n"
		+ "LIMIT :offset , :size")
	List<User> findByStatusNotOffline(@Param("offset") Long offset, @Param("size") Long size);
}
