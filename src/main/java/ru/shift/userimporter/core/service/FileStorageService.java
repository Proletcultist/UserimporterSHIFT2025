package ru.shift.userimporter.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import org.springframework.stereotype.Service;
import ru.shift.userimporter.config.FileStorageProperties;
import ru.shift.userimporter.core.exception.ErrorCode;
import ru.shift.userimporter.core.exception.UserImporterException;

@Service
public class FileStorageService{
	private final Path rootLocation;

	public FileStorageService(FileStorageProperties properties){
		try{
			rootLocation = Paths.get(properties.getLocation()).normalize().toAbsolutePath();
			Files.createDirectories(rootLocation);
		}
		catch (InvalidPathException e){
			throw new UserImporterException("String \""+properties.getLocation()+"\" cannot be converted to Path", ErrorCode.UNEXPECTED_ERROR, e);
		}
		catch (IOException e){
			throw new UserImporterException("Could not initialize storage", ErrorCode.UNEXPECTED_ERROR, e);
		}
	}

	public Path store(InputStream file, String filename){
		Path pathedFilename;
		try{
			pathedFilename = Paths.get(filename);
		}
		catch (InvalidPathException e){
			throw new UserImporterException(ErrorCode.INVALID_FILENAME.getDefaultMessage(), ErrorCode.INVALID_FILENAME, e);
		}


		Path destination = rootLocation.resolve(pathedFilename)
				.normalize().toAbsolutePath();

		if (!destination.getParent().equals(rootLocation)){
			throw new UserImporterException("Cannot store file outside appropriate directory", ErrorCode.INVALID_FILENAME);
		}


		try{
			Files.copy(file, destination);
		}
		catch(IOException e){
			throw new UserImporterException(ErrorCode.STORAGE_ERROR.getDefaultMessage(), ErrorCode.STORAGE_ERROR, e);
		}

		return destination;
	}
}
