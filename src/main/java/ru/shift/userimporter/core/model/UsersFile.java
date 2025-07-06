package ru.shift.userimporter.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Builder;

@Entity
@Table(name = "uploaded_files")
@Data
@Builder
public class UsersFile{

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
}
