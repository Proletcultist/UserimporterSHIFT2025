package ru.shift.userimporter.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.time.LocalDateTime.now;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
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
import ru.shift.userimporter.core.util.UserParser;

@Service
@RequiredArgsConstructor
public class FileProcessingService{

	private final UploadedFileRepository uploadedFileRepository;
	private final FileProcessingErrorRepository errorRepository;
	private final UserService userService;

	@Async
	public void processFile(UsersFile file){

		uploadedFileRepository.updateStatus(IN_PROGRESS, file.getId());

		FileProcessingResult res = new FileProcessingResult();

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file.getStoragePath())))){
			iterateThroughFile(file.getId(), reader, res);
		}
		catch (IOException e){
			uploadedFileRepository.updateRowsInfo(res.getInsertedRows(), res.getUpdatedRows(), file.getId());
			uploadedFileRepository.updateStatus(FAILED, file.getId());
			return;
		}

		uploadedFileRepository.updateRowsInfo(res.getInsertedRows(), res.getUpdatedRows(), file.getId());
		uploadedFileRepository.updateStatus(DONE, file.getId());
	}

	private void iterateThroughFile(long fileId, BufferedReader reader, FileProcessingResult res) throws IOException{
		String line;
		int lineNumber = 1;
		while ((line = reader.readLine()) != null){
			User newUser;

			try{
				newUser = UserParser.parseUser(line);
			}
			catch (UserImporterException e){
				errorRepository.save(FileProcessingError.builder()
							.fileId(fileId)
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
				res.incrementInsertedRowsCounter();
			}
			else{
				res.incrementUpdatedRowsCounter();
			}

			lineNumber++;
		}
	}

	@Getter
	private class FileProcessingResult{
		private int insertedRows = 0;
		private int updatedRows = 0;

		public void incrementInsertedRowsCounter(){ insertedRows++; }
		public void incrementUpdatedRowsCounter(){ updatedRows++; }
	}
}

