package ru.shift.userimporter.core.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import ru.shift.userimporter.core.exception.UserValidationException;
import ru.shift.userimporter.core.model.RawUser;

//TODO: Replace all String.matches() calls with private final Patterns, compiled on initialization of instance and Matcher.matches() calls
public class UserValidator{

	public static void validateRawUser(RawUser user) throws UserValidationException{
	}

	private static void validateFirstName(String name) throws UserValidationException{
		if (name.length() < 3 || name.length() > 50){
			throw new UserValidationException("Length must be from 3 to 50 characters", "INVALID_NAME");
		}
		else if (!name.matches("[а-яА-Я'\\- ]*")){
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
		else if (!name.matches("[а-яА-Я'\\- ]*")){
			throw new UserValidationException("Invalid character", "INVALID_LAST_NAME");
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserValidationException("First character isn't capital cyrillic character", "INVALID_LAST_NAME");
		}
	}

	private static void validateMiddleName(String name) throws UserValidationException{
		if (name.length() == 0){
			return;
		}

		if (name.length() < 3 || name.length() > 50){
			throw new UserValidationException("Length must be from 3 to 50 characters", "INVALID_MIDDLE_NAME");
		}
		else if (!name.matches("[а-яА-Я'\\- ]*")){
			throw new UserValidationException("Invalid character", "INVALID_MIDDLE_NAME");
		}
		else if (name.charAt(0) < 'А' || name.charAt(0) > 'Я'){
			throw new UserValidationException("First character isn't capital cyrillic character", "INVALID_MIDDLE_NAME");
		}
	}

	private static void validateEmail(String email) throws UserValidationException{
		if (!email.matches("[A-Za-z0-9._%\\-]+@[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+")){
			throw new UserValidationException("Invalid email", "INVALID_EMAIL");
		}
		else if (!email.matches("[A-Za-z0-9._%\\-]+@(shift\\.com|shift\\.ru)")){
			throw new UserValidationException("Invalid mail server, only shift.com and shift.ru are allowed", "INVALID_EMAIL");
		}
	}

	private static void validatePhone(String phone) throws UserValidationException{
		if (!phone.matches("\\d*")){
			throw new UserValidationException("Invalid phone", "INVALID_PHONE");
		}
		else if (phone.length() != 0 && (phone.length() > 11 || phone.charAt(0) != '7')){
			throw new UserValidationException("Invalid country code, only 7 is allowed", "INVALID_PHONE");
		}
	}

	private static void validateBirthDate(String birthDateStr) throws UserValidationException{
		if (!birthDateStr.matches("\\d{4}-\\d{2}-\\d{2}")){
			throw new UserValidationException("Invalid birth date", "INVALID_BIRTHDATE");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
		LocalDate now = LocalDate.now();

		if (!birthDate.isBefore(now)){
			throw new UserValidationException("Birth date is before current date", "INVALID_BIRTHDATE");
		}
		else if (Period.between(birthDate, now).getYears() < 18){
			throw new UserValidationException("Users' age must be greater or equal 18 years", "INVALID_BIRTHDATE");
		}
	}
}
