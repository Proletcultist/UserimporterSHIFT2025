package ru.shift.userimporter.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;

@Entity
@Table(name = "uploaded_files")
public class UsersFile{

	@Id
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


	public Integer getId(){ return id; }
	public Integer getInsertedRows(){ return insertedRows; }
	public Integer getUpdatedRows(){ return updatedRows; }
	public String getOriginalFilename(){ return originalFilename; }
	public String getStoragePath(){ return storagePath; }
	public String getStatus(){ return status; }
	public String getHash(){ return hash; }

	public void setId(Integer id){ this.id = id; }
	public void setInsertedRows(Integer insertedRows){ this.insertedRows = insertedRows; }
	public void setUpdatedRows(Integer updatedRows){ this.updatedRows = updatedRows; }
	public void setOriginalFilename(String originalFilename){ this.originalFilename = originalFilename; }
	public void setStoragePath(String storagePath){ this.storagePath = storagePath; }
	public void setStatus(String status){ this.status = status; }
	public void setHash(String hash){ this.hash = hash; }
}
