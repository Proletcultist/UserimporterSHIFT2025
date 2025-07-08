package ru.shift.userimporter.api.dto;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.shift.userimporter.api.dto.FileStatistics;
import ru.shift.userimporter.api.dto.FileStatus;

@Builder
public record FileInfoDto(
	String fileId,
	FileStatus status,
	FileStatistics statistic,
	@JsonProperty("hashCode") String hash
){}
