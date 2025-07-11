package ru.shift.userimporter.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.api.dto.FileResponseDto;
import ru.shift.userimporter.api.dto.FileInfoDto;
import ru.shift.userimporter.api.dto.FileInfoDetailedDto;
import ru.shift.userimporter.core.service.FileService;
import ru.shift.userimporter.api.mapper.UsersFileMapper;
import ru.shift.userimporter.core.model.FileStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController{

	private final FileService fileService;
	private final UsersFileMapper usersFileMapper;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public FileResponseDto postFile(@RequestParam("file") MultipartFile file){
		return usersFileMapper.toFileResponseDto(fileService.storeUsersFile(file));
	}

	@GetMapping("/statistics")
	public List<FileInfoDto> getStatistics(@RequestParam("status") FileStatus status){
		return fileService.getByStatus(status).stream()
			.map(usersFile -> usersFileMapper.toFileInfoDto(usersFile)
			).collect(Collectors.toList());
	}

	@PostMapping("/{fileId}/processing")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void processFile(@PathVariable("fileId") @Positive Long fileId){
		fileService.startFileProcessing(fileId.longValue());
	}

	@GetMapping("/{fileId}/statistics")
	public FileInfoDetailedDto gitDetailedStatistics(@PathVariable("fileId") @Positive Long fileId){
		return usersFileMapper.toFileInfoDetailedDto(fileService.getById(fileId));
	}
}
