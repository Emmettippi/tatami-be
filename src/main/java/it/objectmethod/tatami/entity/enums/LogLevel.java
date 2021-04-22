package it.objectmethod.tatami.entity.enums;

public enum LogLevel {
	ALL(0) // log level Sombra
	, TRACE(1) // log level Tracer
	, DEBUG(2) // log level Winston
	, INFO(3) // log level Orisa
	, WARNING(4) // log level D.Va
	, ERROR(5) // log level Mei
	, FATAL(6) // log level Bastion
	, OFF(7) // log level Ana
	;

	private int logLevel;

	private LogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public int logLevel() {
		return logLevel;
	}
}
