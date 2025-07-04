package ru.shift.userimporter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.storage")
public class FilesStorageProperties{

	private String location;

	public String getLocation(){ return location; }
	public void setLocation(String location){ this.location = location; }
}
