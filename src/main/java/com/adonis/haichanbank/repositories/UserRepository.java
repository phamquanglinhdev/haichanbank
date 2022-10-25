package com.adonis.haichanbank.repositories;

import com.adonis.haichanbank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByCard(String card);

    User findByPhone(String phone);

    User findByToken(String token);
}
