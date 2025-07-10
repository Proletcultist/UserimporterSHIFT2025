package ru.shift.userimporter.core.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import ru.shift.userimporter.core.exception.UserImporterException;
import ru.shift.userimporter.core.exception.ErrorCode;
import ru.shift.userimporter.core.model.RawUser;

public class UserValidator{

	private static final Pattern namesPattern = Pattern.compile("[а-яА-Я'\\- ]*");
	private static final Pattern generalEmailPattern = Pattern.compile("[A-Za-z0-9._%\\-]+@[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+");
	private static final Pattern shiftEmailPattern = Pattern.compile("[A-Za-z0-9._%\\-]+@(shift\\.com|shift\\.ru)");
	private static final Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static void validateRawUser(RawUser user){
		validateFirstName(user.firstName());
		validateLastName(user.lastName());
		validateMiddleName(user.middleName());
		validateEmail(user.email());
		validatePhone(user.phone());
		validateBirthDate(user.birthDate());
	}

	private static void validateFirstName(String name){
		if (name.length() < 3 || name.length() > 50){
			throw new UserImporterException("Length must be from 3 to 50 characters", ErrorCode.INVALID_NAME);
		}
		else if (!namesPattern.matcher(name).matches()){
			throw new UserImporterException("Invalid character", ErrorCode.INVALID_NAME);
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserImporterException("First character isn't capital cyrillic character", ErrorCode.INVALID_NAME);
		}
	}

	private static void validateLastName(String name){
		if (name.length() < 3 || name.length() > 50){
			throw new UserImporterException("Length must be from 3 to 50 characters", ErrorCode.INVALID_LAST_NAME);
		}
		else if (!namesPattern.matcher(name).matches()){
			throw new UserImporterException("Invalid character", ErrorCode.INVALID_LAST_NAME);
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserImporterException("First character isn't capital cyrillic character", ErrorCode.INVALID_LAST_NAME);
		}
	}

	private static void validateMiddleName(String name){
		if (name.isEmpty()){
			return;
		}

		if (name.length() < 3 || name.length() > 50){
			throw new UserImporterException("Length must be from 3 to 50 characters", ErrorCode.INVALID_MIDDLE_NAME);
		}
		else if (!namesPattern.matcher(name).matches()){
			throw new UserImporterException("Invalid character", ErrorCode.INVALID_MIDDLE_NAME);
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserImporterException("First character isn't capital cyrillic character", ErrorCode.INVALID_MIDDLE_NAME);
		}
	}

	private static void validateEmail(String email){
		if (!generalEmailPattern.matcher(email).matches()){
			throw new UserImporterException("Invalid email", ErrorCode.INVALID_EMAIL);
		}
		else if (!shiftEmailPattern.matcher(email).matches()){
			throw new UserImporterException("Invalid mail server, only shift.com and shift.ru are allowed", ErrorCode.INVALID_EMAIL);
		}
	}

	private static void validatePhone(String phone){
		if (!UserValidator.isNumeric(phone)){
			throw new UserImporterException("Invalid phone", ErrorCode.INVALID_PHONE);
		}
		else if (phone.length() != 11 || phone.charAt(0) != '7'){
			throw new UserImporterException("Invalid country code, only 7 is allowed", ErrorCode.INVALID_PHONE);
		}
	}

	private static void validateBirthDate(String birthDateStr){
		if (!datePattern.matcher(birthDateStr).matches()){
			throw new UserImporterException("Invalid birth date", ErrorCode.INVALID_BIRTHDATE);
		}

		LocalDate birthDate = LocalDate.parse(birthDateStr, dateFormatter);
		LocalDate now = LocalDate.now();

		if (!birthDate.isBefore(now)){
			throw new UserImporterException("Birth date is before current date", ErrorCode.INVALID_BIRTHDATE);
		}
		else if (Period.between(birthDate, now).getYears() < 18){
			throw new UserImporterException("Users' age must be greater or equal 18 years", ErrorCode.INVALID_BIRTHDATE);
		}
	}

	private static boolean isNumeric(String str){
		if (str == null || str.isEmpty()){
			return false;
		}

		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}

		return true;
	}
}
