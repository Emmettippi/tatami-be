package it.objectmethod.tatami.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sun.istack.NotNull;

import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.entity.enums.PercentageStatus;
import lombok.Data;

@Data
@Entity
@Table(name = "percentage_progress")
public class Percentage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "operation")
	private PercentageOperation operation;

	@ManyToOne
	@JoinColumn(nullable = false, name = "related_to")
	private User relatedTo;

	@Column(name = "locked")
	private Boolean locked;

	@ManyToOne
	@JoinColumn(nullable = true, name = "locked_by")
	private User lockedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, name = "created_at")
	@CreationTimestamp
	protected Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdated;

	@Column(name = "progression")
	private Double progression;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "progression_status")
	private PercentageStatus progressionStatus;

	@OneToMany(mappedBy = "percentage", fetch = FetchType.LAZY)
	private List<PercentageError> percentageErrors;

	@OneToMany(mappedBy = "percentage", fetch = FetchType.LAZY)
	private List<PercentageQueryParams> percentageQueryParams;
}
