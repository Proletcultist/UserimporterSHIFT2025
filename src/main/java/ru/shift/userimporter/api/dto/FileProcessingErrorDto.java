package ru.shift.userimporter.api.dto;

public record FileProcessingErrorDto(
	int lineNumber,
	String errorCode,
	String errorMessage
){}
