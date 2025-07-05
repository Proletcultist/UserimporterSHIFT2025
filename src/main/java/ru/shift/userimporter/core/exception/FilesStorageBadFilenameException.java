package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FilesStorageException;

public class FilesStorageBadFilenameException extends FilesStorageException{

	public FilesStorageBadFilenameException(String msg){
		super(msg);
	}

	public FilesStorageBadFilenameException(String msg, Throwable t){
		super(msg, t);
	}
}
