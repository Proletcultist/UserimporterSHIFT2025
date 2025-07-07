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
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import ru.shift.userimporter.core.model.FileProcessingError;

@Entity
@Table(name = "uploaded_files")
@Data
@Builder
@AllArgsConstructor
public class UsersFile{

	public UsersFile(){}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "inserted_rows")
	private Integer insertedRows;

	@Column(name = "updated_rows")
	private Integer updatedRows;

	@Column(name = "original_filename")
	private String originalFilename;

	@Column(name = "storage_path")
	private String storagePath;

	private String status;
	private String hash;

	@OneToMany(cascade = CascadeType.ALL)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "file_id")
	private List<FileProcessingError> errors;
}
