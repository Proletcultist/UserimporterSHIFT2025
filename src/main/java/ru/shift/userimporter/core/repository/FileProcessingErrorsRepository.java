package ru.shift.userimporter.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.FileProcessingError;

@Repository
public interface FileProcessingErrorsRepository extends JpaRepository<FileProcessingError, Integer>{}
