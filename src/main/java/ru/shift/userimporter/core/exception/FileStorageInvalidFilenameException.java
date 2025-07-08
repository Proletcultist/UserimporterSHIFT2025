package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FileStorageException;

public class FileStorageInvalidFilenameException extends FileStorageException{

	public FileStorageInvalidFilenameException(String msg){
		super(msg);
	}

	public FileStorageInvalidFilenameException(String msg, Throwable t){
		super(msg, t);
	}
}
