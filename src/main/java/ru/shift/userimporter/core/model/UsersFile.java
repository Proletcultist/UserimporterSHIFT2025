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

	@Column(name = "total_rows")
	private Integer totalRows;

	@Column(name = "processed_rows")
	private Integer processedRows;

	@Column(name = "valid_rows")
	private Integer validRows;

	@Column(name = "invalid_rows")
	private Integer invalidRows;

	@Column(name = "original_filename")
	private String originalFilename;

	@Column(name = "storage_path")
	private String storagePath;

	private String status;
	private String hash;


	public Integer getId(){ return id; }
	public Integer getTotalRows(){ return totalRows; }
	public Integer getProcessedRows(){ return processedRows; }
	public Integer getValidRows(){ return validRows; }
	public Integer getInvalidRows(){ return invalidRows; }
	public String getOriginalFilename(){ return originalFilename; }
	public String getStoragePath(){ return storagePath; }
	public String getStatus(){ return status; }
	public String getHash(){ return hash; }

	public void setId(Integer id){ this.id = id; }
	public void setTotalRows(Integer totalRows){ this.totalRows = totalRows; }
	public void setProcessedRows(Integer processedRows){ this.processedRows = processedRows; }
	public void setValidRows(Integer validRows){ this.validRows = validRows; }
	public void setInvalidRows(Integer invalidRows){ this.invalidRows = invalidRows; }
	public void setOriginalFilename(String originalFilename){ this.originalFilename = originalFilename; }
	public void setStoragePath(String storagePath){ this.storagePath = storagePath; }
	public void setStatus(String status){ this.status = status; }
	public void setHash(String hash){ this.hash = hash; }
}
