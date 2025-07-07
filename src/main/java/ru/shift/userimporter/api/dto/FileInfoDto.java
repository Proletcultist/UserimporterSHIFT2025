package ru.shift.userimporter.api.dto;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.shift.userimporter.api.dto.FileStatistics;

@Builder
public record FileInfoDto(String fileId,
				String status,
				FileStatistics statistic,
				@JsonProperty("hashCode") String hash){}
