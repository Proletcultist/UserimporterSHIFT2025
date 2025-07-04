package ru.shift.userimporter.core.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Table(name = "file_processing_errors")
@Data
public class FileProcessingError{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
