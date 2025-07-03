package ru.shift.userimporter.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.UsersFile;

@Repository
interface UploadedFilesTable extends JpaRepository<UsersFile, Integer>{
	UsersFile save(UsersFile usersFile);

	Optional<UsersFile> findById(Integer id);
}
