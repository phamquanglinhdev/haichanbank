package com.adonis.haichanbank.repositories;

import com.adonis.haichanbank.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByTransactionID(String TransactionID);
}