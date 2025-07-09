package ru.shift.userimporter.core.repository;

import java.time.format.DateTimeFormatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import ru.shift.userimporter.core.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx");

	@Query(value = "INSERT INTO users" +
		"(first_name, last_name, middle_name, email, phone, birth_date, created_at, updated_at)" + 
		"VALUES(" +
		":#{#user.getFirstName()}, " + 
		":#{#user.getLastName()}, " + 
		":#(#user.getMiddleName(), " +
		":#{#user.getEmail()}, " +
		":#{#user.getPhone()}, " +
		":#{#user.getBirthDate().format(dateFormatter)}, " +
		":#{#user.getCreatedAt().format(timestampFormatter)}, " +
		":#{#user.getUpdatedAt().format(timestampFormatter)}, " +
		")" +
		"ON CONFLICT(phone) DO UPDATE SET " +
		"first_name = EXCLUDED.first_name, " +
		"last_name = EXCLUDED.last_name, " +
		"middle_name = EXCLUDED.middle_name, " +
		"email = EXCLUDED.email, " +
		"phone = EXCLUDED.phone, " +
		"birth_date = EXCLUDED.birth_date, " +
		"updated_at = EXCLUDED.updated_at " +
		"RETURNING *", nativeQuery = true)
	User saveOrUpdate(User user);
}
