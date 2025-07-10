package ru.shift.userimporter.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shift.userimporter.core.model.FileProcessingError;
import ru.shift.userimporter.api.dto.FileProcessingErrorDto;

@Mapper(componentModel = "spring")
public interface FileProcessingErrorMapper{

	@Mapping(source = "rowNumber", target = "lineNumber")
	FileProcessingErrorDto toFileProcessingErrorDto(FileProcessingError error);
}
