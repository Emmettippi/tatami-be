package it.objectmethod.tatami.dto;

import lombok.Data;

@Data
public class TatamiDto {

	private Long id;
	private String[][] board;

	public void rotate() {
		this.rotate(true);
	}

	public void rotate(boolean clockwise) {
		int i = 0;
		while (i < board.length / 2) {
			for (int j = i; j < board.length - i - 1; j++) {
				String tmp = board[i][j];
				if (clockwise) {
					board[i][j] = board[board.length - j - 1][i];
					board[board.length - j - 1][i] = board[board.length - i - 1][board.length - j - 1];
					board[board.length - i - 1][board.length - j - 1] = board[j][board.length - i - 1];
					board[j][board.length - i - 1] = tmp;
				} else {
					board[i][j] = board[j][board.length - i - 1];
					board[j][board.length - i - 1] = board[board.length - i - 1][board.length - j - 1];
					board[board.length - i - 1][board.length - j - 1] = board[board.length - j - 1][i];
					board[board.length - j - 1][i] = tmp;
				}
			}
			i++;
		}
	}
}
