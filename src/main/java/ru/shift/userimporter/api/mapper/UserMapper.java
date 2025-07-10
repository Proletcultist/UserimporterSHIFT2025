package ru.shift.userimporter.api.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.api.dto.UserDto;

@Component
public class UserMapper{
	
	private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-ddEHH:mm:ss.SSS");

	public UserDto toUserDto(User user){
		return UserDto.builder()
			.phone(Long.parseLong(user.getPhone()))
			.name(user.getFirstName())
			.lastName(user.getLastName())
			.middleName(user.getMiddleName())
			.email(user.getEmail())
			.birthdate(user.getBirthDate().format(DATE_FORMATTER))
			.creationTime(user.getCreatedAt().format(TIMESTAMP_FORMATTER))
			.updateTime(user.getUpdatedAt().format(TIMESTAMP_FORMATTER))
			.build();
	}

}
