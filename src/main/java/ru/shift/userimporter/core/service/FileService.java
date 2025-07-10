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
import static java.time.LocalDateTime.now;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.io.IOUtils;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.model.UsersFile;
import static ru.shift.userimporter.core.model.FileStatus.*;
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
	private final UploadedFileRepository uploadedFileRepository;
	private final FileProcessingErrorRepository errorRepository;
	private final UserService userService;

	// Loads file to local storage
	// File is named according to its' hash and current timestamp
	// Then, creates entry in DB for it
	// Returns UsersFile object, which represents DB entry, belongs to this file
	public UsersFile storeUsersFile(MultipartFile file){

		// Check if file is empty
		if (file.isEmpty()){
			throw new UserImporterException("File is empty", ErrorCode.INVALID_FILE);
		}

		String hash = calculateFileHash(file);

		// Check if this file is already exists
		if (isFileAlreadyUploaded(file, hash)){
			throw new UserImporterException(ErrorCode.FILE_ALREADY_EXISTS.getDefaultMessage(), ErrorCode.FILE_ALREADY_EXISTS);
		}

		Path storedFile = saveFileToStorage(file, generateStoringFilename(hash));

		// Inserting new entry for this file into DB
		UsersFile newEntry = UsersFile.builder()
			.insertedRows(0)
			.updatedRows(0)
			.originalFilename(file.getOriginalFilename())
			.storagePath(storedFile.toString())
			.status(NEW)
			.hash(hash)
			.build();

		return uploadedFileRepository.save(newEntry);
	}

	@Transactional(readOnly = true)
	public List<UsersFile> getByStatus(String status){
		return uploadedFileRepository.findByStatusWithErrors(status);
	}

	@Transactional(readOnly = true)
	public UsersFile getById(long fileId){
		return uploadedFileRepository.findByIdWithErrors(fileId).orElseThrow(() -> new UserImporterException(ErrorCode.NO_SUCH_FILE.getDefaultMessage(), ErrorCode.NO_SUCH_FILE));
	}

	// Search for file in DB
	// Starts processing
	public void startFileProcessing(long fileId){

		UsersFile file = uploadedFileRepository.findById(fileId).orElseThrow(() -> new UserImporterException(ErrorCode.NO_SUCH_FILE.getDefaultMessage(), ErrorCode.NO_SUCH_FILE));

		processFile(file);

	}

	@Async
	private void processFile(UsersFile file){

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

	private String calculateFileHash(MultipartFile file){
		try{
			return MultipartFileUtils.hashMultipartFile(file);
		}
		catch (IOException e){
			throw new UserImporterException("Failed hashing uploaded file", ErrorCode.FILE_SERVICE_ERROR);
		}
	}

	private Path saveFileToStorage(MultipartFile file, String storingFilename){
		try (InputStream inputStream = file.getInputStream()){
			return storage.store(inputStream, storingFilename);
		}
		catch (IOException e){
			throw new UserImporterException("Failed to open uploaded file", ErrorCode.FILE_SERVICE_ERROR);
		}
	}

	// Needs precalculated file hash
	@Transactional(readOnly = true)
	private boolean isFileAlreadyUploaded(MultipartFile file, String hash){

		for (UsersFile uploadedFile : uploadedFileRepository.findByHash(hash)){
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
		return String.format("%s_%d.csv", hash, Instant.now().getEpochSecond());
	}
}

