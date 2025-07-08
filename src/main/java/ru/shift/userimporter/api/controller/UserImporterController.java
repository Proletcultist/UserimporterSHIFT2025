package ru.shift.userimporter.api.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.api.dto.PostFileResponseDto;
import ru.shift.userimporter.api.dto.FileInfoDto;
import ru.shift.userimporter.core.service.FileService;
import ru.shift.userimporter.api.mapper.UsersFileMapper;
import ru.shift.userimporter.core.exception.InvalidFileStatusException;

@RestController
@RequiredArgsConstructor
public class UserImporterController{

	private final FileService fileService;
	private final UsersFileMapper usersFileMapper;

	@PostMapping("/files")
	@ResponseStatus(value = HttpStatus.CREATED)
	public PostFileResponseDto postFile(@RequestParam("file") MultipartFile file){
		return usersFileMapper.usersFileToPostFileResponseDto(fileService.storeUsersFile(file));
	}

	@GetMapping("/files/statistics")
	public List<FileInfoDto> getStatistics(@RequestParam("status") String status){
		// Check if status is allowed
		if (!(status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE") || status.equals("FAILED"))){
			throw new InvalidFileStatusException("Invalid file status");
		}
		return usersFileMapper.listOfUsersFilesToListOfFileInfoDtos(fileService.getByStatus(status));
	}
}
