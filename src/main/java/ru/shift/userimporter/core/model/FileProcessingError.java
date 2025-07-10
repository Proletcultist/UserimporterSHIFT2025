package ru.shift.userimporter.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "file_processing_errors")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileProcessingError{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// This one doesn't working, can't really figure out why
	// Error says, what where are two logical columns,
	// reffering to one column in DB "file_id", but there is only one...
	@Column(name = "file_id")
	private long fileId;

	private int rowNumber;

	private String errorMessage;

	private String errorCode;

	private String rawData;
}
