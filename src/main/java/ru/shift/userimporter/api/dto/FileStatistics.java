package ru.shift.userimporter.api.dto;

import lombok.Builder;

@Builder
public record FileStatistics(
	Integer insertedLinesCount,
	Integer updatedLinesCount,
	Integer errorProcessedLinesCount
){}
