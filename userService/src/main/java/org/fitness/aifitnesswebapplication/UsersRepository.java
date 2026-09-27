package org.fitness.aifitnesswebapplication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.fitness.aifitnesswebapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User,String> {
    Boolean existsByEmail(String email);

    Boolean existsByKeycloakId(String userId);

    User findByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);
}
