package ru.shift.userimporter.core.exception;

import lombok.Getter;

@Getter
public class UserValidationException extends Exception{

	private final String errorCode;

	public UserValidationException(String msg, String errorCode){
		super(msg);
		this.errorCode = errorCode;
	}

	public UserValidationException(String msg, String errorCode,  Throwable t){
		super(msg, t);
		this.errorCode = errorCode;
	}
}
