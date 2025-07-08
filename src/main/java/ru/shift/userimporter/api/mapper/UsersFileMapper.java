package ru.shift.userimporter.api.mapper;

import java.util.List;
//import java.util.stream.*;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shift.userimporter.api.dto.PostFileResponseDto;
import ru.shift.userimporter.api.dto.FileInfoDto;
import ru.shift.userimporter.api.dto.FileStatistics;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.api.dto.FileStatus;

@Mapper(componentModel = "spring")
public interface UsersFileMapper{

	@Mapping(source = "id", target = "fileId") 
	PostFileResponseDto usersFileToPostFileResponseDto(UsersFile usersFile);

	default FileInfoDto usersFileToFileInfoDto(UsersFile usersFile){
		return FileInfoDto.builder()
			.fileId(String.valueOf(usersFile.getId()))
			.status(FileStatus.valueOf(usersFile.getStatus()))
			.statistic(
					FileStatistics.builder()
					.insertedLinesCount(usersFile.getInsertedRows())
					.updatedLinesCount(usersFile.getUpdatedRows())
					.errorProcessedLinesCount(usersFile.getErrors().size())
					.build()
				  )
			.hash(usersFile.getHash())
			.build();
	}

	default List<FileInfoDto> listOfUsersFilesToListOfFileInfoDtos(List<UsersFile> usersFiles){
		return usersFiles.stream().map(usersFile -> {
			return usersFileToFileInfoDto(usersFile);
		}).collect(Collectors.toList());
	}
}
