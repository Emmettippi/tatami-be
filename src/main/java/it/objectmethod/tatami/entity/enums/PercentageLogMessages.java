package it.objectmethod.tatami.entity.enums;

public enum PercentageLogMessages {
	ARXIVAR_RESOURCE_NOT_DOWNLOADED(LogLevel.ERROR), NO_RESOURCE_FOR_TRANSCRIPT(LogLevel.ERROR), EXPORT_FAILED(
		LogLevel.FATAL), EXPORT_HAS_ZERO_RECORDS(LogLevel.INFO);

	private LogLevel logLevel;

	private PercentageLogMessages(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public LogLevel logMessage() {
		return logLevel;
	}
}
