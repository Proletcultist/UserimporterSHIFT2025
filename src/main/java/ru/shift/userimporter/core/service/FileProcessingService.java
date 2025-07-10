package ru.shift.userimporter.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.time.LocalDateTime.now;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import static ru.shift.userimporter.core.model.FileStatus.*;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.model.FileProcessingError;
import ru.shift.userimporter.core.exception.UserImporterException;
import ru.shift.userimporter.core.exception.ErrorCode;
import ru.shift.userimporter.core.repository.FileProcessingErrorRepository;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.service.UserService;

@Service
@RequiredArgsConstructor
public class FileProcessingService{

	private final UploadedFileRepository uploadedFileRepository;
	private final FileProcessingErrorRepository errorRepository;
	private final UserService userService;

	@Async
	public void processFile(UsersFile file){

		uploadedFileRepository.updateStatus(IN_PROGRESS, file.getId());

		String line;
		int lineNumber = 1, inserted = 0, updated = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file.getStoragePath())))){
			while ((line = reader.readLine()) != null){
				User newUser;

				try{
					newUser = userService.parseUser(line);
				}
				catch (UserImporterException e){
					errorRepository.save(FileProcessingError.builder()
								.fileId(file.getId())
								.rowNumber(lineNumber)
								.errorMessage(e.getMessage())
								.errorCode(e.getErrorCode().name())
								.rawData(line)
								.build()
								);
					lineNumber++;
					continue;
				}

				newUser.setCreatedAt(now());
				newUser.setUpdatedAt(newUser.getCreatedAt());

				User userInRepo = userService.updateUser(newUser);

				if (userInRepo.getCreatedAt().isEqual(userInRepo.getUpdatedAt())){
					inserted++;
				}
				else{
					updated++;
				}

				lineNumber++;
			}
		}
		catch (IOException e){
			uploadedFileRepository.updateStatus(FAILED, file.getId());
		}

		uploadedFileRepository.updateRowsInfo(inserted, updated, file.getId());
		uploadedFileRepository.updateStatus(DONE, file.getId());
	}

}
