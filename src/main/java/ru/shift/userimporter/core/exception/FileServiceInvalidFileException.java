package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FileServiceException;

public class FileServiceInvalidFileException extends FileServiceException{

	public FileServiceInvalidFileException(String msg){
		super(msg);
	}

	public FileServiceInvalidFileException(String msg, Throwable t){
		super(msg, t);
	}
}
