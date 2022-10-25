package com.adonis.haichanbank.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity(name = "History")
@Table(name = "histories")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false)
    private User from;
    @ManyToOne
    @JoinColumn(name = "to_id", nullable = false)
    private User to;

    @Column(nullable = false)
    private int amount;
    private String message;

    @Column(nullable = false)
    private Timestamp created = Timestamp.from(Instant.now());

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public History() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                '}';
    }
}