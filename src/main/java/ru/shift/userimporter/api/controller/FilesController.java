package ru.shift.userimporter.api.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.api.dto.PostFileResponseDto;
import ru.shift.userimporter.api.dto.FileInfoDto;
import ru.shift.userimporter.core.service.FileService;
import ru.shift.userimporter.api.mapper.UsersFileMapper;
import ru.shift.userimporter.core.exception.InvalidFileStatusException;
import ru.shift.userimporter.api.dto.FileStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FilesController{

	private final FileService fileService;
	private final UsersFileMapper usersFileMapper;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public PostFileResponseDto postFile(@RequestParam("file") MultipartFile file){
		return usersFileMapper.usersFileToPostFileResponseDto(fileService.storeUsersFile(file));
	}

	@GetMapping("/statistics")
	public List<FileInfoDto> getStatistics(@RequestParam("status") FileStatus status){
		return usersFileMapper.listOfUsersFilesToListOfFileInfoDtos(fileService.getByStatus(status.name()));
	}
}
