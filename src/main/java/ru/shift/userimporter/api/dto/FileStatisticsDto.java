package ru.shift.userimporter.api.dto;

import lombok.Builder;

@Builder
public record FileStatisticsDto(
	Integer insertedLinesCount,
	Integer updatedLinesCount,
	Integer errorProcessedLinesCount
){}
