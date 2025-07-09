package ru.shift.userimporter.core.exception;

import ru.shift.userimporter.core.exception.FileServiceException;

public class FileServiceNoSuchFileException extends FileServiceException{
	public FileServiceNoSuchFileException(String msg){
		super(msg);
	}

	public FileServiceNoSuchFileException(String msg, Throwable t){
		super(msg, t);
	}
}
