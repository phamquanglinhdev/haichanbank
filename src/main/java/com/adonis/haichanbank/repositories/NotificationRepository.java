package com.adonis.haichanbank.repositories;

import com.adonis.haichanbank.models.Notification;
import com.adonis.haichanbank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedDesc(User user);
}