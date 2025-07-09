package ru.shift.userimporter.api.dto;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.shift.userimporter.api.dto.FileStatisticsDto;
import ru.shift.userimporter.api.dto.FileStatus;

@Builder
public record FileInfoDto(
	String fileId,
	FileStatus status,
	FileStatisticsDto statistic,
	@JsonProperty("hashCode") String hash
){}
