package ru.shift.userimporter.core.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.UsersFile;

@Repository
public interface UploadedFileRepository extends JpaRepository<UsersFile, Long>{
	List<UsersFile> findByHash(String hash);

	@Query("SELECT f FROM UsersFile f LEFT JOIN FETCH f.errors WHERE f.status = :status")
	List<UsersFile> findByStatusWithErrors(String status);

	@Modifying
	@Transactional
	@Query("UPDATE UsersFile f SET f.status = :status WHERE f.id = :fileId")
	void updateStatus(String status, long fileId);

	@Modifying
	@Transactional
	@Query("UPDATE UsersFile f SET f.insertedRows = :insertedRows, f.updatedRows = :updatedRows WHERE f.id = :fileId")
	void updateRowsInfo(int insertedRows, int updatedRows, long fileId);
}
