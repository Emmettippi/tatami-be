package it.objectmethod.tatami.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.objectmethod.tatami.entity.Lobby;
import it.objectmethod.tatami.entity.enums.LobbyType;

public class RowMappers {

	public static RowMapper<Lobby> getLobbyRowMapper() {
		return new RowMapper<Lobby>() {
			@Override
			public Lobby mapRow(ResultSet rs, int rowNum) throws SQLException {
				Lobby lobby = new Lobby();
				lobby.setId(rs.getLong("id"));
				lobby.setLastInLobby1(rs.getLong("last_in_lobby_1"));
				lobby.setLastInLobby2(rs.getLong("last_in_lobby_2"));
				lobby.setLastInLobby3(rs.getLong("last_in_lobby_3"));
				lobby.setLastInLobby4(rs.getLong("last_in_lobby_4"));
				lobby.setUserId1(rs.getLong("user_1_id"));
				lobby.setUserId2(rs.getLong("user_2_id"));
				lobby.setUserId3(rs.getLong("user_3_id"));
				lobby.setUserId4(rs.getLong("user_4_id"));
				lobby.setLobbyName(rs.getString("lobby_name"));
				String lobbyTypeStr = rs.getString("lobby_type");
				if (!Utils.isBlank(lobbyTypeStr)) {
					lobby.setLobbyType(LobbyType.valueOf(lobbyTypeStr));
				}
				return lobby;
			}
		};
	}
}
