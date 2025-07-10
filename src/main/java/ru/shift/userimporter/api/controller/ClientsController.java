package ru.shift.userimporter.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.service.UserService;
import ru.shift.userimporter.api.mapper.UserMapper;
import ru.shift.userimporter.api.dto.UserDto;
import ru.shift.userimporter.core.model.UserSearchFilter;
import ru.shift.userimporter.core.util.UserValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientsController{

	private final int DEFAULT_LIMIT = 100;
	private final int DEFAULT_OFFSET = 0;
	
	private final UserService userService;
	private final UserMapper userMapper;

	@GetMapping
	public List<UserDto> getUsers(@RequestParam(name = "phone", required = false) Long phone,
					@RequestParam(name = "name", required = false) String name,
					@RequestParam(name = "lastName", required = false) String lastName,
					@RequestParam(name = "email", required = false) String email,
					@RequestParam(name = "limit", required = false) Integer limit,
					@RequestParam(name = "offset", required = false) Integer offset){

		UserSearchFilter filter = UserSearchFilter.builder()
						.phone(phone)
						.name(name)
						.lastName(lastName)
						.email(email)
						.limit(limit == null ? DEFAULT_LIMIT : limit.intValue())
						.offset(offset == null ? DEFAULT_OFFSET : offset.intValue())
						.build();

		UserValidator.validateUserSearchFilter(filter);

		return userService.getByFilter(filter).stream()
							.map(user -> userMapper.toUserDto(user))
							.collect(Collectors.toList());
	}
}
