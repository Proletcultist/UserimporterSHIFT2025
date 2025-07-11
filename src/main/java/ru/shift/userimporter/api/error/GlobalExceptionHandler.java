package ru.shift.userimporter.api.error;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import ru.shift.userimporter.core.exception.UserImporterException;
import ru.shift.userimporter.core.exception.ErrorCode;
import ru.shift.userimporter.api.dto.ErrorDto;
import ru.shift.userimporter.core.model.FileStatus;

@RestControllerAdvice
public class GlobalExceptionHandler{

	@ExceptionHandler(value = UserImporterException.class)
	public ResponseEntity<ErrorDto> handleUserImporterException(UserImporterException e){
		ResponseEntity.BodyBuilder builder;

		switch (e.getErrorCode()){
			case INVALID_FILENAME:
			case INVALID_FILE:
				builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
				break;
			case FILE_ALREADY_EXISTS:
				builder = ResponseEntity.status(HttpStatus.CONFLICT);
				break;
			case NO_SUCH_FILE:
				builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
				break;
			case STORAGE_ERROR:
			case FILE_SERVICE_ERROR:
			case UNEXPECTED_ERROR:
			default:
				builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
				break;
		}

		return builder.body(new ErrorDto(e.getMessage()));
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

	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorDto noHandler(NoHandlerFoundException e){
		return new ErrorDto("Not found");
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto argumentTypeMismatch(MethodArgumentTypeMismatchException e){
		return new ErrorDto("Invalid type of paramter");
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto argumentNotValid(MethodArgumentNotValidException e){
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult()
			.getFieldErrors()
			.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		return new ErrorDto(
				errors.entrySet()
					.stream()
					.map(entry -> entry.getKey() + ": " + entry.getValue())
					.collect(Collectors.joining(", "))
				);

	}

	@ExceptionHandler(value = HandlerMethodValidationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto argumentNotValid(HandlerMethodValidationException e){
		Map<String, String> errors = new HashMap<>();

		e.getParameterValidationResults()
			.forEach(validationRes -> {
				String param = validationRes.getMethodParameter().getParameterName();

				String paramErrors = validationRes.getResolvableErrors().stream()
									.map(error -> error.getDefaultMessage())
									.collect(Collectors.joining(", "));
				errors.put(param, paramErrors);
				
			});

		return new ErrorDto(
				errors.entrySet()
					.stream()
					.map(entry -> entry.getKey() + ": " + entry.getValue())
					.collect(Collectors.joining("; "))
				);

	}

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

}
