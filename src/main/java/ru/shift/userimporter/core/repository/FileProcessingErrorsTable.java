package ru.shift.userimporter.core.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.FileProcessingError;

@Repository
interface FileProcessingErrorsTable extends CrudRepository<FileProcessingError, Integer>{
	FileProcessingError save(FileProcessingError error);

	Optional<FileProcessingError> findById(Integer id);
}
