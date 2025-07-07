package ru.shift.userimporter.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import lombok.Setter;
import ru.shift.userimporter.api.dto.PostFileResponseDto;
import ru.shift.userimporter.core.service.FileService;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.api.mapper.UsersFileMapper;

@RestController
@Setter
public class UserImporterController{

	@Autowired
	private FileService fileService;

	@Autowired
	private UsersFileMapper usersFileMapper;

	@PostMapping("/files")
	@ResponseStatus(value = HttpStatus.CREATED)
	public PostFileResponseDto postFile(@RequestParam("file") MultipartFile file){
		return usersFileMapper.usersFileToPostFileResponseDto(fileService.storeUsersFile(file));
	}

}
