package ru.shift.userimporter.api.dto;

import lombok.Builder;

@Builder
public record UserDto(
	long phone,
	String name,
	String lastName,
	String middleName,
	String email,
	String birthdate,
	String creationTime,
	String updateTime
){}
