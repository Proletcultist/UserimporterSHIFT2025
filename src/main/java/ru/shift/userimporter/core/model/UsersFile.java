package ru.shift.userimporter.core.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import ru.shift.userimporter.core.model.FileProcessingError;

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

	@Column(name = "inserted_rows")
	private int insertedRows;

	@Column(name = "updated_rows")
	private int updatedRows;

	@Column(name = "original_filename")
	private String originalFilename;

	@Column(name = "storage_path")
	private String storagePath;

	private String status;
	private String hash;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "file_id")
	private List<FileProcessingError> errors;
}
