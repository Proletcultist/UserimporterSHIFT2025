package ru.shift.userimporter.core.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.User;

@Repository
interface UsersTable extends CrudRepository<User, Integer>{
	User save(User user);

	Optional<User> findById(Integer id);
}
