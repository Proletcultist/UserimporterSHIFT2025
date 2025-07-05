package ru.shift.userimporter.core.exception;

public class FileServiceException extends RuntimeException{

	public FileServiceException(String msg){
		super(msg);
	}

	public FileServiceException(String msg, Throwable t){
		super(msg, t);
	}
}
