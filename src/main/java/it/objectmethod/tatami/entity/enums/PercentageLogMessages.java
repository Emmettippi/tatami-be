package it.objectmethod.tatami.entity.enums;

public enum PercentageLogMessages {
	LOBBY_IS_CLOSED(LogLevel.WARNING), NONEXISTEND_LOBBY(LogLevel.WARNING);

	private LogLevel logLevel;

	private PercentageLogMessages(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public LogLevel logMessage() {
		return logLevel;
	}
}
