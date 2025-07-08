package ru.shift.userimporter.core.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.repository.UsersRepository;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.model.RawUser;
import ru.shift.userimporter.core.exception.UserValidationException;

@Service
@RequiredArgsConstructor
public class UserService{

	private final UsersRepository users;

	public void updateUser(User user){
	}

	/*
	public User parseUser(String str){
	}

	private RawUser parseRawUser(String str){
	}

	private User rawUserToUser(RawUser rawUser){
	}
	*/

	private void validateRawUser(RawUser user) throws UserValidationException{
	}

}
