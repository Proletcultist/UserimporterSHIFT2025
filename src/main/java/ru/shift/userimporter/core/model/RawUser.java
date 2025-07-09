package ru.shift.userimporter.core.model;

public record RawUser(
	String firstName,
	String lastName,
	String middleName,
	String email,
	String phone,
	String birthDate){}
