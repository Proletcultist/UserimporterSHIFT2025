package ru.shift.userimporter.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ru.shift.userimporter.core.util.RawUser;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.util.UserValidator;
import ru.shift.userimporter.core.exception.UserImporterException;
import static ru.shift.userimporter.core.exception.ErrorCode.*;

public class UserParser{

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static User parseUser(String str){
		RawUser raw = parseRawUser(str);

		UserValidator.validateRawUser(raw);

		return rawUserToUser(raw);
	}


	private static RawUser parseRawUser(String str){
		String splitted[] = str.split(",");
		if (splitted.length != 6){
			throw new UserImporterException("Wrong amount of fields", INVALID_FORMAT);
		}

		return RawUser.builder()
			.firstName(splitted[0])
			.lastName(splitted[1])
			.middleName(splitted[2])
			.email(splitted[3])
			.phone(splitted[4])
			.birthDate(splitted[5])
			.build();
	}

	private static User rawUserToUser(RawUser rawUser){
		return User.builder()
			.firstName(rawUser.firstName())
			.lastName(rawUser.lastName())
			.middleName(rawUser.middleName())
			.email(rawUser.email())
			.phone(rawUser.phone())
			.birthDate(LocalDate.parse(rawUser.birthDate(), DATE_FORMATTER))
			.build();
	}

}
