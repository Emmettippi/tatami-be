package it.objectmethod.tatami.entity.enums;

public enum PercentageLogMessages {
	LOBBY_IS_CLOSED(LogLevel.WARNING), NONEXISTEND_LOBBY(LogLevel.WARNING), LOBBY_IS_PRIVATE(
		LogLevel.WARNING), UNABLE_TO_JOIN(LogLevel.WARNING);

	private LogLevel logLevel;

	private PercentageLogMessages(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public LogLevel logMessage() {
		return logLevel;
	}
}
