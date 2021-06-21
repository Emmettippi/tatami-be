package it.objectmethod.tatami.service;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.utils.Utils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTService {

	private static final String MY_SECRET_JWT_KEY = "orb-is-hide-and-bonus";

	public String createJWTToken(User user) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(Utils.now());

		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);

		Algorithm alg = Algorithm.HMAC256(MY_SECRET_JWT_KEY);
		String token = JWT.create()
			.withClaim("user_id", user.getId())
			.withClaim("username", user.getUsername())
			.withExpiresAt(cal.getTime()).sign(alg);

		return token;
	}

	public boolean checkJWTToken(String jwtToken) {
		boolean valid = false;
		Algorithm alg = Algorithm.HMAC256(MY_SECRET_JWT_KEY);
		try {
			JWTVerifier ver = JWT.require(alg).build();
			DecodedJWT decoded = ver.verify(jwtToken);

			Long userId = decoded.getClaim("user_id").asLong();
			String userName = decoded.getClaim("username").asString();

			log.debug("Utente verificato! " + userId + " | " + userName);
			valid = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return valid;
	}

	public Long getUserIdByToken(String jwtToken) {
		Long userId = null;
		Algorithm alg = Algorithm.HMAC256(MY_SECRET_JWT_KEY);
		try {
			JWTVerifier ver = JWT.require(alg).build();
			DecodedJWT decoded = ver.verify(jwtToken);

			userId = decoded.getClaim("user_id").asLong();
			String userName = decoded.getClaim("username").asString();

			log.debug("Utente verificato! " + userId + " - " + userName);
		} catch (Exception e) {
			e.printStackTrace();
			userId = null;
		}
		return userId;
	}
}
