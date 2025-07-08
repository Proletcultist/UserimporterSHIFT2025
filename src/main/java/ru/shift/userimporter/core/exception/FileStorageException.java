package ru.shift.userimporter.core.exception;

public class FileStorageException extends RuntimeException{

	public FileStorageException(String msg){
		super(msg);
	}

	public FileStorageException(String msg, Throwable t){
		super(msg, t);
	}
}
