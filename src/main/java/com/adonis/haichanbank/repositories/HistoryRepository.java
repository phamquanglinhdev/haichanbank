package com.adonis.haichanbank.repositories;

import com.adonis.haichanbank.models.History;
import com.adonis.haichanbank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
        List<History> getHistoriesByFromOrToOrderByCreatedDesc(User from, User to);
}
