package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FileServiceException;

public class FileServiceBadFileException extends FileServiceException{

	public FileServiceBadFileException(String msg){
		super(msg);
	}

	public FileServiceBadFileException(String msg, Throwable t){
		super(msg, t);
	}
}
