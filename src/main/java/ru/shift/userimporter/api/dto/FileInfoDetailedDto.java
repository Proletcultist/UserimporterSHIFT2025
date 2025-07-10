package ru.shift.userimporter.api.dto;

import java.util.List;
import lombok.Builder;
import ru.shift.userimporter.api.dto.FileProcessingErrorDto;

@Builder
public record FileInfoDetailedDto(
	int insertedLinesCount,
	int updatedLinesCount,
	List<FileProcessingErrorDto> errors
){}
