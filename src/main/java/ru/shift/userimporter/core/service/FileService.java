package ru.shift.userimporter.core.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.Runnable;
import java.io.InputStream;
import java.time.Instant;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import com.google.common.hash.Funnels;
import com.google.common.io.ByteStreams;
import com.google.common.hash.Hashing;
import com.google.common.hash.Hasher;
import ru.shift.userimporter.core.model.UsersFile;
import ru.shift.userimporter.core.repository.FilesStorage;
import ru.shift.userimporter.core.repository.UploadedFilesTable;
import ru.shift.userimporter.core.repository.FileProcessingErrorsTable;
import ru.shift.userimporter.core.exception.FileServiceException;

@Service
@Setter
public class FileService{

	@Autowired
	private FilesStorage storage;

	@Autowired
	private UploadedFilesTable uploadedFiles;

	@Autowired
	private FileProcessingErrorsTable processingErrors;

	private ExecutorService threadPool;


	@PostConstruct
	public void init(){
		threadPool = Executors.newCachedThreadPool();
	}

	// Loads file to local storage
	// File is named according to its' id in DB
	// Then, creates entry in DB for it
	// Returns UsersFile object, which represents DB entry, belongs to this file
	public UsersFile storeUsersFile(MultipartFile file){

		String hash;

		try (InputStream inputStream = file.getInputStream()){
			Hasher hasher = Hashing.sha256().newHasher();
			ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
			hash = hasher.hash().toString();
		}
		catch (IOException e){
			throw new FileServiceException("Failed hashing uploaded file");
		}

		long currentTime = Instant.now().getEpochSecond();

		String storingFilename = hash + "_" + String.valueOf(currentTime);

		try (InputStream inputStream = file.getInputStream()){
			storage.store(inputStream, storingFilename);
		}
		catch (IOException e){
			throw new FileServiceException("Failed to open uploaded file");
		}

		// TODO: Add entry to the DB

		return new UsersFile();
	}

	// Search for file in DB
	// Check if file is existing
	// Initialize some reader class with file bufferized and pass it to new thread, processing it
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

