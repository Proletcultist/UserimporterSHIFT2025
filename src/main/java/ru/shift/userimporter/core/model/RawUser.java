package ru.shift.userimporter.core.model;

import lombok.Builder;

@Builder
public record RawUser(
	String firstName,
	String lastName,
	String middleName,
	String email,
	String phone,
	String birthDate){}
