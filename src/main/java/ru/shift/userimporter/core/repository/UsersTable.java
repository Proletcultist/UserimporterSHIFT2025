package ru.shift.userimporter.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.User;

@Repository
public interface UsersTable extends JpaRepository<User, Integer>{
	User save(User user);

	Optional<User> findById(Integer id);
}
