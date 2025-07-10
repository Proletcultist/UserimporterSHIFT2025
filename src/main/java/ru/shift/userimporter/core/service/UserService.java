package ru.shift.userimporter.core.service;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.repository.UserRepository;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.model.RawUser;
import ru.shift.userimporter.core.util.UserValidator;
import ru.shift.userimporter.core.exception.UserImporterException;
import ru.shift.userimporter.core.exception.ErrorCode;
import ru.shift.userimporter.core.model.UserSearchFilter;
import ru.shift.userimporter.core.repository.OffsetBasedPageRequest;

@Service
@RequiredArgsConstructor
public class UserService{

	private final UserRepository users;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public User updateUser(User user){
		return users.saveOrUpdate(user);
	}

	public User parseUser(String str){
		RawUser raw = parseRawUser(str);

		UserValidator.validateRawUser(raw);

		return rawUserToUser(raw);
	}

	public List<User> getByFilter(UserSearchFilter filter){

		User user = User.builder()
				.firstName(filter.name())
				.lastName(filter.lastName())
				.email(filter.email())
				.phone(filter.phone() == null ? null : filter.phone().toString())
				.build();

		ExampleMatcher matcher = ExampleMatcher.matching()
							.withIgnorePaths("id")
							.withIgnoreNullValues();
		Example criteria = Example.of(user, matcher);

		OffsetBasedPageRequest paging = new OffsetBasedPageRequest(filter.offset(), filter.limit());

		return users.findAll(criteria, paging).getContent();
	}

	private RawUser parseRawUser(String str){
		String splitted[] = str.split(",");
		if (splitted.length != 6){
			throw new UserImporterException("Wrong amount of fields", ErrorCode.INVALID_FORMAT);
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

	private User rawUserToUser(RawUser rawUser){
		return User.builder()
			.id(0)
			.firstName(rawUser.firstName())
			.lastName(rawUser.lastName())
			.middleName(rawUser.middleName())
			.email(rawUser.email())
			.phone(rawUser.phone())
			.birthDate(LocalDate.parse(rawUser.birthDate(), DATE_FORMATTER))
			.createdAt(null)
			.updatedAt(null)
			.build();
	}

}
