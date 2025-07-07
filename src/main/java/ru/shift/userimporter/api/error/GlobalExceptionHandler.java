package ru.shift.userimporter.api.error;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import ru.shift.userimporter.core.exception.FileServiceBadFileException;
import ru.shift.userimporter.core.exception.FileServiceFileAlreadyExistException;
import ru.shift.userimporter.core.exception.FileServiceException;
import ru.shift.userimporter.core.exception.FilesStorageException;
import ru.shift.userimporter.core.exception.FilesStorageBadFilenameException;
import ru.shift.userimporter.api.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler{

	@ExceptionHandler(value = FilesStorageBadFilenameException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto badFilename(FilesStorageBadFilenameException e){
		return new ErrorDto(e.getMessage());
	}
	
	@ExceptionHandler(value = FilesStorageException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto storageError(FilesStorageException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = FileServiceBadFileException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto badFile(FileServiceBadFileException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = FileServiceFileAlreadyExistException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public ErrorDto fileAlreadyExist(FileServiceFileAlreadyExistException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = FileServiceException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto fileServiceError(FileServiceException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = MultipartException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto notAMultipart(MultipartException e){
		return new ErrorDto("Current request is not a multipart request");
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto notAMultipart(HttpRequestMethodNotSupportedException e){
		return new ErrorDto("Method not allowed");
	}

	/*
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto unexpectedException(Exception e){
		return new ErrorDto("Unexpected error");
	}

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto unexpectedRuntimeException(RuntimeException e){
		return new ErrorDto("Unexpected error");
	}
	*/

}
