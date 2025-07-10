package ru.shift.userimporter.api.mapper;

import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.shift.userimporter.api.dto.FileResponseDto;
import ru.shift.userimporter.api.dto.FileInfoDto;
import ru.shift.userimporter.api.dto.FileStatisticsDto;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.api.dto.FileInfoDetailedDto;
import ru.shift.userimporter.api.mapper.FileProcessingErrorMapper;
import ru.shift.userimporter.core.model.FileProcessingError;

@Mapper(componentModel = "spring")
public interface UsersFileMapper{

	@Mapping(source = "id", target = "fileId") 
	FileResponseDto toFileResponseDto(UsersFile usersFile);

	default FileInfoDto toFileInfoDto(UsersFile usersFile){
		return FileInfoDto.builder()
			.fileId(String.valueOf(usersFile.getId()))
			.status(FileStatus.valueOf(usersFile.getStatus()))
			.statistic(
					FileStatisticsDto.builder()
					.insertedLinesCount(usersFile.getInsertedRows())
					.updatedLinesCount(usersFile.getUpdatedRows())
					.errorProcessedLinesCount(usersFile.getErrors().size())
					.build()
				  )
			.hash(usersFile.getHash())
			.build();
	}

	default FileInfoDetailedDto toFileInfoDetailedDto(UsersFile usersFile){
		return FileInfoDetailedDto.builder()
			.insertedLinesCount(usersFile.getInsertedRows())
			.updatedLinesCount(usersFile.getUpdatedRows())
			.errors(usersFile.getErrors().stream()
					.map(processingError -> Mappers.getMapper(FileProcessingErrorMapper.class).toFileProcessingErrorDto(processingError))
					.collect(Collectors.toList())
				)
			.build();
	}
}
