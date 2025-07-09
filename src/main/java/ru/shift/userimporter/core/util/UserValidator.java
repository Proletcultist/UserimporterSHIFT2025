package ru.shift.userimporter.core.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import ru.shift.userimporter.core.exception.UserValidationException;
import ru.shift.userimporter.core.model.RawUser;

public class UserValidator{

	private static final Pattern namesPattern = Pattern.compile("[а-яА-Я'\\- ]*");
	private static final Pattern generalEmailPattern = Pattern.compile("[A-Za-z0-9._%\\-]+@[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+");
	private static final Pattern shiftEmailPattern = Pattern.compile("[A-Za-z0-9._%\\-]+@(shift\\.com|shift\\.ru)");
	private static final Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static void validateRawUser(RawUser user) throws UserValidationException{
		validateFirstName(user.firstName());
		validateLastName(user.lastName());
		validateMiddleName(user.middleName());
		validateEmail(user.email());
		validatePhone(user.phone());
		validateBirthDate(user.birthDate());
	}

	private static void validateFirstName(String name) throws UserValidationException{
		if (name.length() < 3 || name.length() > 50){
			throw new UserValidationException("Length must be from 3 to 50 characters", "INVALID_NAME");
		}
		else if (!namesPattern.matcher(name).matches()){
			throw new UserValidationException("Invalid character", "INVALID_NAME");
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserValidationException("First character isn't capital cyrillic character", "INVALID_NAME");
		}
	}

	private static void validateLastName(String name) throws UserValidationException{
		if (name.length() < 3 || name.length() > 50){
			throw new UserValidationException("Length must be from 3 to 50 characters", "INVALID_LAST_NAME");
		}
		else if (!namesPattern.matcher(name).matches()){
			throw new UserValidationException("Invalid character", "INVALID_LAST_NAME");
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserValidationException("First character isn't capital cyrillic character", "INVALID_LAST_NAME");
		}
	}

	private static void validateMiddleName(String name) throws UserValidationException{
		if (name.isEmpty()){
			return;
		}

		if (name.length() < 3 || name.length() > 50){
			throw new UserValidationException("Length must be from 3 to 50 characters", "INVALID_MIDDLE_NAME");
		}
		else if (!namesPattern.matcher(name).matches()){
			throw new UserValidationException("Invalid character", "INVALID_MIDDLE_NAME");
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserValidationException("First character isn't capital cyrillic character", "INVALID_MIDDLE_NAME");
		}
	}

	private static void validateEmail(String email) throws UserValidationException{
		if (!generalEmailPattern.matcher(email).matches()){
			throw new UserValidationException("Invalid email", "INVALID_EMAIL");
		}
		else if (!shiftEmailPattern.matcher(email).matches()){
			throw new UserValidationException("Invalid mail server, only shift.com and shift.ru are allowed", "INVALID_EMAIL");
		}
	}

	private static void validatePhone(String phone) throws UserValidationException{
		if (!UserValidator.isNumeric(phone)){
			throw new UserValidationException("Invalid phone", "INVALID_PHONE");
		}
		else if (phone.length() != 11 || phone.charAt(0) != '7'){
			throw new UserValidationException("Invalid country code, only 7 is allowed", "INVALID_PHONE");
		}
	}

	private static void validateBirthDate(String birthDateStr) throws UserValidationException{
		if (!datePattern.matcher(birthDateStr).matches()){
			throw new UserValidationException("Invalid birth date", "INVALID_BIRTHDATE");
		}

		LocalDate birthDate = LocalDate.parse(birthDateStr, dateFormatter);
		LocalDate now = LocalDate.now();

		if (!birthDate.isBefore(now)){
			throw new UserValidationException("Birth date is before current date", "INVALID_BIRTHDATE");
		}
		else if (Period.between(birthDate, now).getYears() < 18){
			throw new UserValidationException("Users' age must be greater or equal 18 years", "INVALID_BIRTHDATE");
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
