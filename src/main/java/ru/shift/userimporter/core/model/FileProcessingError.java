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
	private Integer id;

	@Column(name = "file_id")
	private Integer fileId;

	@Column(name = "row_number")
	private Integer rowNumber;

	@Column(name = "error_message")
	private String errorMessage;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "raw_data")
	private String rawData;
}
