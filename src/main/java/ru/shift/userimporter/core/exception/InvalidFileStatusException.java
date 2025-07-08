package ru.shift.userimporter.core.exception;

public class InvalidFileStatusException extends RuntimeException{
	public InvalidFileStatusException(String msg){
		super(msg);
	}

	public InvalidFileStatusException(String msg, Throwable t){
		super(msg, t);
	}
}
