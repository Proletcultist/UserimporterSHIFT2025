package ru.shift.userimporter.core.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.lang.Runnable;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.time.Instant;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PreDestroy;
import com.google.common.hash.Funnels;
import com.google.common.io.ByteStreams;
import com.google.common.hash.Hashing;
import com.google.common.hash.Hasher;
import org.apache.commons.io.IOUtils;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.core.repository.FilesStorage;
import ru.shift.userimporter.core.repository.UploadedFilesRepository;
import ru.shift.userimporter.core.repository.FileProcessingErrorsRepository;
import ru.shift.userimporter.core.exception.FileServiceException;
import ru.shift.userimporter.core.exception.FileServiceInvalidFileException;
import ru.shift.userimporter.core.exception.FileServiceFileAlreadyExistException;

@Service
@RequiredArgsConstructor
public class FileService{

	private final FilesStorage storage;
	private final UploadedFilesRepository uploadedFiles;
	private final FileProcessingErrorsRepository processingErrors;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	// Loads file to local storage
	// File is named according to its' hash and current timestamp
	// Then, creates entry in DB for it
	// Returns UsersFile object, which represents DB entry, belongs to this file
	public UsersFile storeUsersFile(MultipartFile file) throws FileServiceInvalidFileException,
	       								FileServiceFileAlreadyExistException{

		// Check if file is empty
		if (file.isEmpty()){
			throw new FileServiceInvalidFileException("Failed to store empty file");
		}

		// Hashing file
		String hash;

		try (InputStream inputStream = file.getInputStream()){
			Hasher hasher = Hashing.murmur3_128().newHasher();
			ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
			hash = hasher.hash().toString();
		}
		catch (IOException e){
			throw new FileServiceException("Failed hashing uploaded file");
		}

		// Check if this file is already exists
		boolean foundEqual = false;
		for (UsersFile uploadedFile : uploadedFiles.findByHash(hash)){
			if (uploadedFile.getHash().equals(hash)){
				try (InputStream uploadedFileIS = new FileInputStream(uploadedFile.getStoragePath());
						InputStream fileIS = file.getInputStream()){

					if (IOUtils.contentEquals(uploadedFileIS, fileIS)){
						foundEqual = true;
						break;
					}	

				}
				catch (FileNotFoundException e){
					throw new FileServiceException("Failed to open stored file");
				}
				catch (IOException e){
					throw new FileServiceException("Failed when comparing new and stored file");
				}
			}
		}

		if (foundEqual){
			throw new FileServiceFileAlreadyExistException("File already exist");
		}

		long currentTime = Instant.now().getEpochSecond();

		String storingFilename = hash + "_" + String.valueOf(currentTime);

		// Storing file
		Path storedFile;
		try (InputStream inputStream = file.getInputStream()){
			storedFile = storage.store(inputStream, storingFilename);
		}
		catch (IOException e){
			throw new FileServiceException("Failed to open uploaded file");
		}

		// Inserting new entry for this file into DB
		UsersFile newEntry = UsersFile.builder()
			.id(null)
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
	// Check if file is existing
	// Starts processing in new thread in pool
	public void startFileProcessing(UsersFile searchReq){


		class ProcessFileRunnable implements Runnable{
			// Processes file row by row
			// If row processed successfully, adds new user or updates existing one
			// If row processed unsuccessfully, adds info about error to DB
			// Collects statistic and updates file info in DB afterwords
			@Override
			public void run(){
			}
		}
		// Run ProcessFileRunnable in pool
	}

	@PreDestroy
	public void cleanup(){
		threadPool.shutdown();
	}
}

