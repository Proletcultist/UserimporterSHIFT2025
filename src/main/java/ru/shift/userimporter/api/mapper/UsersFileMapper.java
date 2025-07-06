package ru.shift.userimporter.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shift.userimporter.api.dto.PostFileResponseDto;
import ru.shift.userimporter.core.model.UsersFile;

@Mapper(componentModel = "spring")
public interface UsersFileMapper{

	@Mapping(source = "id", target = "fileId") 
	PostFileResponseDto usersFileToPostFileResponseDto(UsersFile usersFile);

}
