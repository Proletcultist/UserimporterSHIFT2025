package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FilesStorageException;

public class FilesStorageInvalidFilenameException extends FilesStorageException{

	public FilesStorageInvalidFilenameException(String msg){
		super(msg);
	}

	public FilesStorageInvalidFilenameException(String msg, Throwable t){
		super(msg, t);
	}
}
