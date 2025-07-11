package ru.shift.userimporter.core.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.repository.UserRepository;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.model.UserSearchFilter;
import ru.shift.userimporter.core.repository.OffsetBasedPageRequest;

@Service
@RequiredArgsConstructor
public class UserService{

	private final UserRepository userRepository;

	public User updateUser(User user){
		return userRepository.saveOrUpdate(user);
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

		return userRepository.findAll(criteria, paging).getContent();
	}
}
