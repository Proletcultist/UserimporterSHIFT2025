package ru.shift.userimporter.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping("/ping")
	public static String ping(){
		return "Pong";
	}

	@PostMapping("/files")
	public PostFileResponseDto postFile(@RequestParam("file") MultipartFile file){
		return usersFileMapper.usersFileToPostFileResponseDto(fileService.storeUsersFile(file));
	}

}
