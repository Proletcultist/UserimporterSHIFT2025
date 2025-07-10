package ru.shift.userimporter.core.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.config.FileStorageProperties;
import ru.shift.userimporter.core.exception.FileStorageException;
import ru.shift.userimporter.core.exception.FileStorageInvalidFilenameException;

@Repository
public class FileStorage{
	private final Path rootLocation;

	public FileStorage(FileStorageProperties properties) throws FileStorageException{
		try{
			rootLocation = Paths.get(properties.getLocation()).normalize().toAbsolutePath();
			Files.createDirectories(rootLocation);
		}
		catch (InvalidPathException e){
			throw new FileStorageException("String \""+properties.getLocation()+"\" cannot be converted to Path", e);
		}
		catch (IOException e){
			throw new FileStorageException("Could not initialize storage", e);
		}
	}

	public Path store(InputStream file, String filename) throws FileStorageException,
	       								FileStorageInvalidFilenameException{
		Path pathedFilename;
		try{
			pathedFilename = Paths.get(filename);
		}
		catch (InvalidPathException e){
			throw new FileStorageInvalidFilenameException("Invalid filename", e);
		}


		Path destination = rootLocation.resolve(pathedFilename)
				.normalize().toAbsolutePath();

		if (!destination.getParent().equals(rootLocation)){
			throw new FileStorageInvalidFilenameException("Cannot store file outside appropriate directory");
		}


		try{
			Files.copy(file, destination);
		}
		catch(IOException e){
			throw new FileStorageException("Failed to store file", e);
		}

		return destination;
	}
}
