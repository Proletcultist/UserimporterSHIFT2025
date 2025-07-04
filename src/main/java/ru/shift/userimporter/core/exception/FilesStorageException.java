package ru.shift.userimporter.core.exception;

public class FilesStorageException extends RuntimeException{

	public FilesStorageException(String msg){
		super(msg);
	}

	public FilesStorageException(String msg, Throwable t){
		super(msg, t);
	}
}
