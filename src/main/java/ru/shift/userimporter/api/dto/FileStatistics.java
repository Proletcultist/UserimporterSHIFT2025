package ru.shift.userimporter.api.dto;

import lombok.Data;

@Data
public class FileStatistics{
	Integer insertedLinesCount;
	Integer updatedLinesCount;
	Integer errorProcessedLinesCount;
}
