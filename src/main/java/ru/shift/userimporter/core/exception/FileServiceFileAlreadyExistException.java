package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FileServiceException;

public class FileServiceFileAlreadyExistException extends FileServiceException{

	public FileServiceFileAlreadyExistException(String msg){
		super(msg);
	}

	public FileServiceFileAlreadyExistException(String msg, Throwable t){
		super(msg, t);
	}
}
