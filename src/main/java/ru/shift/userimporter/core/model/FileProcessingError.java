package ru.shift.userimporter.core.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.shift.userimporter.core.model.UsersFile;

@Entity
@Table(name = "file_processing_errors")
@Data
@Builder
@AllArgsConstructor
public class FileProcessingError{

	public FileProcessingError(){}

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

	/*
	@ManyToOne
	private UsersFile usersFile;
	*/
}
