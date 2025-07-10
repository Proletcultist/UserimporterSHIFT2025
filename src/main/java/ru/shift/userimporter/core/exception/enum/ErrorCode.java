package ru.shift.userimporter.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode{
	FILE_ALREADY_EXISTS("File already exists"),
	INVALID_FILE("Invalid file provided"),
	NO_SUCH_FILE("File doesn't exists"),
	INVALID_FILENAME("Invalid filename"),
	INVALID_FORMAT("Invalid format of user"),
	INVALID_NAME("Invalid user name"),
	INVALID_LAST_NAME("Invalid user last name"),
	INVALID_MIDDLE_NAME("Invalid user middle name"),
	INVALID_EMAIL("Invalid user email"),
	INVALID_PHONE("Invalid user phone"),
	INVALID_BIRTHDATE("Invalid user birth date"),
	STORAGE_ERROR("File storage operation failed"),
	FILE_SERVICE_ERROR("File service error occured"),
	UNEXPECTED_ERROR("Unexpected error occured");

	private final String defaultMessage;
}
