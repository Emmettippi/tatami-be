package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import it.objectmethod.tatami.entity.enums.LogLevel;
import it.objectmethod.tatami.entity.enums.PercentageLogMessages;
import lombok.Data;

@Data
@Entity
@Table(name = "percentage_errors")
public class PercentageError {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "message_type")
	private LogLevel messageType;

	public void setMessageType(LogLevel messageType) {
		this.messageType = messageType;
		this.setMessageTypeId(Long.valueOf(messageType.logLevel()));
	}

	@Column(name = "message_type_id")
	@NotNull
	private Long messageTypeId;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "message_key")
	private PercentageLogMessages messageKey;

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

	@Column(name = "extra_notes")
	private String extraNotes;

	@Column(name = "extra_param_1")
	private String extraParam1;

	@Column(name = "extra_param_2")
	private String extraParam2;

	@Column(name = "extra_param_3")
	private String extraParam3;

	@Column(name = "extra_param_4")
	private String extraParam4;

	@ManyToOne
	@JoinColumn(nullable = false, name = "percentage_id")
	private Percentage percentage;
}
