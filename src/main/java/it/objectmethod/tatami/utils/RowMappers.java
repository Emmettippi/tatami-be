package it.objectmethod.tatami.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.objectmethod.tatami.dto.UserSearchResponseDto;
import it.objectmethod.tatami.entity.Lobby;
import it.objectmethod.tatami.entity.enums.LobbyType;
import it.objectmethod.tatami.entity.enums.UserStatus;

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
				lobby.setFriends(rs.getLong("sum_friends"));
				String lobbyTypeStr = rs.getString("lobby_type");
				if (!Utils.isBlank(lobbyTypeStr)) {
					lobby.setLobbyType(LobbyType.valueOf(lobbyTypeStr));
				}
				return lobby;
			}
		};
	}

	public static RowMapper<UserSearchResponseDto> getUserSearchRowMapper() {
		return new RowMapper<UserSearchResponseDto>() {
			@Override
			public UserSearchResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserSearchResponseDto usrDto = new UserSearchResponseDto();
				usrDto.setCommonFriends(rs.getInt("common_friends"));
				usrDto.setEmail(rs.getString("email"));
				usrDto.setId(rs.getLong("id"));
				usrDto.setLastOnline(rs.getDate("last_online"));
				usrDto.setNickname(rs.getString("nickname"));
				usrDto.setProfileImage(rs.getBytes("profile_image"));
				usrDto.setUsername(rs.getString("username"));
				String userStatusStr = rs.getString("user_status");
				if (!Utils.isBlank(userStatusStr)) {
					usrDto.setUserStatus(UserStatus.valueOf(userStatusStr));
				}
				return usrDto;
			}
		};
	}
}
