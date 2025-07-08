package ru.shift.userimporter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.FileProcessingError;

@Repository
public interface FileProcessingErrorRepository extends JpaRepository<FileProcessingError, Long>{}
