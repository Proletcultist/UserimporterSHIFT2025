package ru.shift.userimporter.core.model;

import lombok.Builder;

@Builder
public record UserSearchFilter(
	Long phone,
	String name,
	String lastName,
	String email,
	int limit,
	int offset
){}
