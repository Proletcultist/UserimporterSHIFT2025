package ru.shift.userimporter.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.core.model.FileStatus;

@Repository
public interface UploadedFileRepository extends JpaRepository<UsersFile, Long>{
	@Query("SELECT f FROM UsersFile f LEFT JOIN FETCH f.errors WHERE f.status = :status")
	List<UsersFile> findByStatusWithErrors(FileStatus status);

	@Query("SELECT f FROM UsersFile f LEFT JOIN FETCH f.errors WHERE f.id = :fileId")
	Optional<UsersFile> findByIdWithErrors(long fileId);

	boolean existsByHash(String hash);

	@Modifying
	@Transactional
	@Query("UPDATE UsersFile f SET f.status = :status WHERE f.id = :fileId")
	void updateStatus(FileStatus status, long fileId);

	@Modifying
	@Transactional
	@Query("UPDATE UsersFile f SET f.insertedRows = :insertedRows, f.updatedRows = :updatedRows WHERE f.id = :fileId")
	void updateRowsInfo(int insertedRows, int updatedRows, long fileId);
}
