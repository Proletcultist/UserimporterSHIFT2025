package ru.shift.userimporter.core.exception;

import lombok.Getter;
import ru.shift.userimporter.core.exception.ErrorCode;

@Getter
public class UserImporterException extends RuntimeException{

	private final ErrorCode errorCode;

	public UserImporterException(String msg, ErrorCode errorCode){
		super(msg);
		this.errorCode = errorCode;
	}

	public UserImporterException(String msg, ErrorCode errorCode, Throwable t){
		super(msg, t);
		this.errorCode = errorCode;
	}

}
