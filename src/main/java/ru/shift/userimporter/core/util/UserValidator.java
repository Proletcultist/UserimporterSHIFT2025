package ru.shift.userimporter.core.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import ru.shift.userimporter.core.exception.UserImporterException;
import static ru.shift.userimporter.core.exception.ErrorCode.*;
import ru.shift.userimporter.core.model.RawUser;
import ru.shift.userimporter.core.model.UserSearchFilter;

public class UserValidator{

	private static final Pattern NAMES_PATTERN = Pattern.compile("[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,49}");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9._%\\-]+@(shift\\.com|shift\\.ru)");
	private static final Pattern PHONE_PATTERN = Pattern.compile("7\\d{10}");
	private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final int MIN_USER_AGE = 18;

	public static void validateRawUser(RawUser user){
		validateFirstName(user.firstName());
		validateLastName(user.lastName());
		validateMiddleName(user.middleName());
		validateEmail(user.email());
		validatePhone(user.phone());
		validateBirthDate(user.birthDate());
	}

	public static void validateUserSearchFilter(UserSearchFilter filter){
		if (filter.phone() != null){
			validatePhone(filter.phone().toString());
		}
		if (filter.name() != null){
			validateFirstName(filter.name());
		}
		if (filter.lastName() != null){
			validateLastName(filter.lastName());
		}
		if (filter.email() != null){
			validateEmail(filter.email());
		}
	}

	private static void validateFirstName(String name){
		if (!NAMES_PATTERN.matcher(name).matches()){
			throw new UserImporterException(INVALID_NAME.getDefaultMessage(),INVALID_NAME);
		}
	}

	private static void validateLastName(String name){
		if (!NAMES_PATTERN.matcher(name).matches()){
			throw new UserImporterException(INVALID_LAST_NAME.getDefaultMessage(), INVALID_LAST_NAME);
		}
	}

	private static void validateMiddleName(String name){
		if (name.isEmpty()){
			return;
		}

		if (!NAMES_PATTERN.matcher(name).matches()){
			throw new UserImporterException(INVALID_MIDDLE_NAME.getDefaultMessage(), INVALID_MIDDLE_NAME);
		}
	}

	private static void validateEmail(String email){
		if (!EMAIL_PATTERN.matcher(email).matches()){
			throw new UserImporterException(INVALID_EMAIL.getDefaultMessage(), INVALID_EMAIL);
		}
	}

	private static void validatePhone(String phone){
		if (!PHONE_PATTERN.matcher(phone).matches()){
			throw new UserImporterException(INVALID_PHONE.getDefaultMessage(), INVALID_PHONE);
		}
	}

	private static void validateBirthDate(String birthDateStr){
		if (!DATE_PATTERN.matcher(birthDateStr).matches()){
			throw new UserImporterException(INVALID_BIRTHDATE.getDefaultMessage(), INVALID_BIRTHDATE);
		}

		LocalDate birthDate = LocalDate.parse(birthDateStr, DATE_FORMATTER);
		LocalDate now = LocalDate.now();

		if (!birthDate.isBefore(now)){
			throw new UserImporterException("Birth date is before current date", INVALID_BIRTHDATE);
		}
		else if (Period.between(birthDate, now).getYears() < MIN_USER_AGE){
			throw new UserImporterException("Users' age must be greater or equal 18 years", INVALID_BIRTHDATE);
		}
	}
}
