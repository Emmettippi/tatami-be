package it.objectmethod.tatami.dto;

import it.objectmethod.tatami.entity.enums.LogLevel;
import it.objectmethod.tatami.entity.enums.PercentageLogMessages;
import lombok.Data;

@Data
public class PercentageErrorDto {
	private Long id;
	private LogLevel messageType;
	private Long messageTypeId;
	private PercentageLogMessages messageKey;
	private String extraNotes;
	private String extraParam1;
	private String extraParam2;
	private String extraParam3;
	private String extraParam4;
	private Long percentageId;

	public void setMessageType(LogLevel messageType) {
		this.messageType = messageType;
		this.setMessageTypeId(Long.valueOf(messageType.logLevel()));
	}

	public void setMessageKey(PercentageLogMessages messageKey) {
		this.messageKey = messageKey;
		if (messageKey != null) {
			this.messageType = messageKey.logMessage();
			this.messageTypeId = Long.valueOf(messageKey.logMessage().logLevel());
		} else {
			this.messageType = null;
			this.messageTypeId = null;
		}
	}
}
