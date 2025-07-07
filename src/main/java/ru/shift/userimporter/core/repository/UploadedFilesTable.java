package ru.shift.userimporter.core.repository;

import java.util.Optional;
import java.util.List;
import java.lang.Iterable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.UsersFile;

@Repository
public interface UploadedFilesTable extends CrudRepository<UsersFile, Integer>{
	UsersFile save(UsersFile usersFile);

	Optional<UsersFile> findById(Integer id);

	Iterable<UsersFile> findByHash(String hash);

	@Query("SELECT f FROM UsersFile f JOIN FETCH f.errors WHERE f.status = :status")
	List<UsersFile> findByStatusWithErrors(String status);
}
