package ru.shift.userimporter.api.error;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.shift.userimporter.core.exception.FileServiceInvalidFileException;
import ru.shift.userimporter.core.exception.FileServiceFileAlreadyExistException;
import ru.shift.userimporter.core.exception.FileServiceException;
import ru.shift.userimporter.core.exception.FilesStorageException;
import ru.shift.userimporter.core.exception.FilesStorageInvalidFilenameException;
import ru.shift.userimporter.core.exception.InvalidFileStatusException;
import ru.shift.userimporter.api.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler{

	@ExceptionHandler(value = FilesStorageInvalidFilenameException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto invalidFilename(FilesStorageInvalidFilenameException e){
		return new ErrorDto(e.getMessage());
	}
	
	@ExceptionHandler(value = FilesStorageException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto storageError(FilesStorageException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = FileServiceInvalidFileException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto invalidFile(FileServiceInvalidFileException e){
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
	public ErrorDto methodNotAllowed(HttpRequestMethodNotSupportedException e){
		return new ErrorDto("Method not allowed");
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto missingQueryParam(MissingServletRequestParameterException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = InvalidFileStatusException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto invalidFileStatus(InvalidFileStatusException e){
		return new ErrorDto(e.getMessage());
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorDto noHandler(NoHandlerFoundException e){
		return new ErrorDto("Not found");
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
