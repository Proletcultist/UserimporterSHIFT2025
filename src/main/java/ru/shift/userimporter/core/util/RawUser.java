package ru.shift.userimporter.core.util;

import lombok.Builder;

@Builder
record RawUser(
	String firstName,
	String lastName,
	String middleName,
	String email,
	String phone,
	String birthDate){}
