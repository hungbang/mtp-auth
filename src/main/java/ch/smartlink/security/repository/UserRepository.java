package ch.smartlink.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.smartlink.security.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String s);
}
