package ru.shift.userimporter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
public class FilesStorageProperties{

	private String location;

}
