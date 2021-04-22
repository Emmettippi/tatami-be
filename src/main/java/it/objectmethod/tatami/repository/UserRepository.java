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

	User findByUsernameAndPassword(String username, String password);

	List<User> findByNicknameContains(String nickname);
}
