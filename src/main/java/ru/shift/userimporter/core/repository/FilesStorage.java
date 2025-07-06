package ru.shift.userimporter.core.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.config.FilesStorageProperties;
import ru.shift.userimporter.core.exception.FilesStorageException;
import ru.shift.userimporter.core.exception.FilesStorageBadFilenameException;

@Repository
public class FilesStorage{
	private final Path rootLocation;

	@Autowired
	public FilesStorage(FilesStorageProperties properties) throws FilesStorageException{
		try{
			rootLocation = Paths.get(properties.getLocation()).normalize().toAbsolutePath();
			Files.createDirectories(rootLocation);
		}
		catch (InvalidPathException e){
			throw new FilesStorageException("String \""+properties.getLocation()+"\" cannot be converted to Path", e);
		}
		catch (IOException e){
			throw new FilesStorageException("Could not initialize storage", e);
		}
	}

	public Path store(InputStream file, String filename) throws FilesStorageException,
	       								FilesStorageBadFilenameException{
		Path pathedFilename;
		try{
			pathedFilename = Paths.get(filename);
		}
		catch (InvalidPathException e){
			throw new FilesStorageBadFilenameException("Bad filename", e);
		}


		Path destination = rootLocation.resolve(pathedFilename)
				.normalize().toAbsolutePath();

		if (!destination.getParent().equals(rootLocation)){
			throw new FilesStorageBadFilenameException("Cannot store file outside appropriate directory");
		}


		try{
			Files.copy(file, destination);
		}
		catch(IOException e){
			throw new FilesStorageException("Failed to store file", e);
		}

		return destination;
	}
}
