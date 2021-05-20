package it.objectmethod.tatami.utils;

public class QueryConstants {

	private QueryConstants() {
	}

	public static final String[] LOBBY_SEARCH = {
		"SELECT lob.id AS id \n"
			+ ", lob.user_1_id AS user_1_id \n"
			+ ", lob.user_2_id AS user_2_id \n"
			+ ", lob.user_3_id AS user_3_id \n"
			+ ", lob.user_4_id AS user_4_id \n"
			+ ", lob.last_in_lobby_1 AS last_in_lobby_1 \n"
			+ ", lob.last_in_lobby_2 AS last_in_lobby_2 \n"
			+ ", lob.last_in_lobby_3 AS last_in_lobby_3 \n"
			+ ", lob.last_in_lobby_4 AS last_in_lobby_4 \n"
			+ ", lob.lobby_type AS lobby_type \n"
			+ ", lob.lobby_name AS lobby_name \n"
			+ ", lob.game_id AS game_id \n"
			+ ", SUM(lob.friends) AS sum_friends"
			+ " FROM (( \n"
			+ "  SELECT l1.*, 1 AS friends FROM lobby l1 \n"
			+ "    JOIN user_info u1 ON u1.id = l1.user_1_id \n"
			+ "    JOIN user_user r1 ON u1.id = r1.user_2_id \n"
			+ "      AND r1.relationship = 'FRIEND' \n"
			+ "      AND r1.user_1_id = ? \n"
			+ ") UNION ALL ( \n"
			+ "  SELECT l2.*, 1 AS friends FROM lobby l2 \n"
			+ "    JOIN user_info u2 ON u2.id = l2.user_2_id \n"
			+ "    JOIN user_user r2 ON u2.id = r2.user_2_id \n"
			+ "      AND r2.relationship = 'FRIEND' \n"
			+ "      AND r2.user_1_id = ? \n"
			+ ") UNION ALL ( \n"
			+ "  SELECT l3.*, 1 AS friends FROM lobby l3 \n"
			+ "    JOIN user_info u3 ON u3.id = l3.user_3_id \n"
			+ "    JOIN user_user r3 ON u3.id = r3.user_2_id \n"
			+ "      AND r3.relationship = 'FRIEND' \n"
			+ "      AND r3.user_1_id = ? \n"
			+ ") UNION ALL ( \n"
			+ "  SELECT l4.*, 1 AS friends FROM lobby l4 \n"
			+ "    JOIN user_info u4 ON u4.id = l4.user_4_id \n"
			+ "    JOIN user_user r4 ON u4.id = r4.user_2_id \n"
			+ "      AND r4.relationship = 'FRIEND' \n"
			+ "      AND r4.user_1_id = ? \n"
			+ ") UNION ALL ( \n"
			+ "  SELECT l.*, 0 AS friends FROM lobby l \n"
			+ "  WHERE l.lobby_type = 'PUBLIC' \n"
			+ ")) lob \n"
			+ "WHERE (lob.user_1_id IS NULL OR lob.user_2_id IS NULL OR lob.user_3_id IS NULL OR lob.user_4_id IS NULL) \n"
			+ "  AND lob.closed = FALSE \n",
		"GROUP BY lob.id \n"
			+ ", lob.user_1_id \n"
			+ ", lob.user_2_id \n"
			+ ", lob.user_3_id \n"
			+ ", lob.user_4_id \n"
			+ ", lob.last_in_lobby_1 \n"
			+ ", lob.last_in_lobby_2 \n"
			+ ", lob.last_in_lobby_3 \n"
			+ ", lob.last_in_lobby_4 \n"
			+ ", lob.lobby_type \n"
			+ ", lob.lobby_name \n"
			+ ", lob.game_id \n"
			+ "ORDER BY SUM(lob.friends) DESC, id ASC \n"
			+ "LIMIT ? , ?" };

	public static final String[] USER_SEARCH = {
		"SELECT u.id AS id, u.username AS username, u.password AS password, u.nickname AS nickname \n" +
			", u.email AS email, u.user_status AS user_status, u.last_online AS last_online, u.profile_image AS profile_image \n"
			+
			", count(uu2.id) AS common_friends \n" +
			"FROM ( \n" +
			"  SELECT u_.* FROM user_info u_ \n" +
			"    LEFT JOIN user_user uu \n" +
			"      ON u_.id = uu.user_1_id \n" +
			"        AND uu.user_2_id = ? \n" +
			"        AND ( uu.relationship = 'FRIEND' \n" +
			"          OR uu.relationship = 'BLOCKED') \n" +
			"  WHERE uu.id IS NULL AND u_.id <> ? \n" +
			") u \n" +
			"  LEFT JOIN user_user uu1 \n" +
			"    ON uu1.user_1_id = u.id AND uu1.relationship = 'FRIEND' \n" +
			"      AND uu1.user_1_id <> ? \n" +
			"  LEFT JOIN user_user uu2 \n" +
			"    ON uu1.user_2_id = uu2.user_1_id AND uu2.relationship = 'FRIEND' \n" +
			"      AND uu2.user_2_id = ? \n" +
			"  LEFT JOIN user_user uu3 \n" +
			"    ON uu2.user_2_id = uu3.user_1_id AND uu3.relationship = 'FRIEND' \n" +
			"      AND uu3.user_2_id = u.id \n" +
			"WHERE u.id <> ? \n" +
			"  AND uu3.id IS NULL \n",
		"GROUP BY u.id, u.username, u.password, u.nickname \n" +
			", u.email, u.user_status, u.last_online, u.profile_image \n" +
			"ORDER BY common_friends DESC, id \n" +
			"LIMIT ? , ?"
	};
}
