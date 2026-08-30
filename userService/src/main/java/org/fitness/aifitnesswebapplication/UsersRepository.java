package org.fitness.aifitnesswebapplication;

import org.fitness.aifitnesswebapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User,String> {
    Boolean existsByEmail(String email);
}
