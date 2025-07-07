package ru.shift.userimporter.api.dto;

import lombok.Data;
import lombok.Builder;
import ru.shift.userimporter.api.dto.FileStatistics;

@Data
@Builder
public class FileInfoDto{
	private String fileId;
	private String status;
	private FileStatistics statistic;
	private String hashCode;
}
