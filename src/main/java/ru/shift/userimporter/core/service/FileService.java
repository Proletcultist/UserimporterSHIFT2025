package ru.shift.userimporter.core.service;

import java.util.List;
import java.io.InputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.apache.commons.io.IOUtils;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.core.service.FileStorageService;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.repository.FileProcessingErrorRepository;
import ru.shift.userimporter.core.exception.UserImporterException;
import ru.shift.userimporter.core.exception.ErrorCode;
import ru.shift.userimporter.core.util.MultipartFileUtils;
import ru.shift.userimporter.core.service.UserService;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.model.FileProcessingError;

@Service
@RequiredArgsConstructor
public class FileService{

	private final FileStorageService storage;
	private final UploadedFileRepository uploadedFiles;
	private final FileProcessingErrorRepository processingErrors;
	private final UserService userService;

	// Loads file to local storage
	// File is named according to its' hash and current timestamp
	// Then, creates entry in DB for it
	// Returns UsersFile object, which represents DB entry, belongs to this file
	public UsersFile storeUsersFile(MultipartFile file){

		// Check if file is empty
		if (file.isEmpty()){
			throw new UserImporterException("Failed to store empty file", ErrorCode.INVALID_FILE);
		}

		// Hashing file
		String hash;
		try{
			hash = MultipartFileUtils.hashMultipartFile(file);
		}
		catch (IOException e){
			throw new UserImporterException("Failed hashing uploaded file", ErrorCode.FILE_SERVICE_ERROR);
		}

		// Check if this file is already exists
		if (repositoryContainsFile(file, hash)){
			throw new UserImporterException(ErrorCode.FILE_ALREADY_EXISTS.getDefaultMessage(), ErrorCode.FILE_ALREADY_EXISTS);
		}

		String storingFilename = generateStoringFilename(hash);

		// Storing file
		Path storedFile;
		try (InputStream inputStream = file.getInputStream()){
			storedFile = storage.store(inputStream, storingFilename);
		}
		catch (IOException e){
			throw new UserImporterException("Failed to open uploaded file", ErrorCode.FILE_SERVICE_ERROR);
		}

		// Inserting new entry for this file into DB
		UsersFile newEntry = UsersFile.builder()
			.id(0)
			.insertedRows(0)
			.updatedRows(0)
			.originalFilename(file.getOriginalFilename())
			.storagePath(storedFile.toString())
			.status("NEW")
			.hash(hash)
			.build();

		return uploadedFiles.save(newEntry);
	}

	public List<UsersFile> getByStatus(String status){
		return uploadedFiles.findByStatusWithErrors(status);
	}

	// Search for file in DB
	// Starts processing
	public void startFileProcessing(long fileId){

		UsersFile file = uploadedFiles.findById(fileId).orElseThrow(() -> new UserImporterException(ErrorCode.NO_SUCH_FILE.getDefaultMessage(), ErrorCode.NO_SUCH_FILE));

		processFile(file);

	}

	@Async
	private void processFile(UsersFile file){

		uploadedFiles.updateStatus("IN_PROGRESS", file.getId());

		String line;
		int lineNumber = 0, inserted = 0, updated = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file.getStoragePath())))){
			while ((line = reader.readLine()) != null){
				User newUser;

				try{
					newUser = userService.parseUser(line);
				}
				catch (UserImporterException e){
					processingErrors.save(FileProcessingError.builder()
								.id(0)
								.fileId(file.getId())
								.rowNumber(lineNumber)
								.errorMessage(e.getMessage())
								.errorCode(e.getErrorCode().name())
								.rawData(line)
								.build()
								);
					continue;
				}

				LocalDateTime now = LocalDateTime.now();

				newUser.setCreatedAt(now);
				newUser.setUpdatedAt(now);

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
			uploadedFiles.updateStatus("FAILED", file.getId());
		}

		uploadedFiles.updateRowsInfo(inserted, updated, file.getId());
		uploadedFiles.updateStatus("DONE", file.getId());
	}

	// Needs precalculated file hash
	private boolean repositoryContainsFile(MultipartFile file, String hash){

		for (UsersFile uploadedFile : uploadedFiles.findByHash(hash)){
			if (uploadedFile.getHash().equals(hash)){
				try (InputStream uploadedFileIS = new FileInputStream(uploadedFile.getStoragePath());
						InputStream fileIS = file.getInputStream()){

					if (IOUtils.contentEquals(uploadedFileIS, fileIS)){
						return true;
					}	

				}
				catch (FileNotFoundException e){
					throw new UserImporterException("Failed to open stored file", ErrorCode.FILE_SERVICE_ERROR);
				}
				catch (IOException e){
					throw new UserImporterException("Failed when comparing new and stored file", ErrorCode.FILE_SERVICE_ERROR);
				}
			}
		}

		return false;
	}

	// Needs precalculated file hash
	private String generateStoringFilename(String hash){
		long currentTime = Instant.now().getEpochSecond();
		return hash + "_" + String.valueOf(currentTime);
	}
}

