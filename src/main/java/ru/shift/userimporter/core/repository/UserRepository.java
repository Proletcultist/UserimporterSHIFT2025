package ru.shift.userimporter.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	// TODO: Add method, implementing INSERT ON CONFLICT DO UPDATE query
}
