package ru.shift.userimporter.core.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import ru.shift.userimporter.core.model.FileProcessingError;
import ru.shift.userimporter.core.model.FileStatus;

@Entity
@Table(name = "uploaded_files")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersFile{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int insertedRows;

	private int updatedRows;

	private String originalFilename;

	private String storagePath;

	@Enumerated(EnumType.STRING)
	private FileStatus status;
	private String hash;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "file_id")
	private List<FileProcessingError> errors;
}
