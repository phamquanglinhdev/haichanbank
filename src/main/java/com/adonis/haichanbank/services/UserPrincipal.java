package com.adonis.haichanbank.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;

public class UserPrincipal extends User {
    @Serial
    private static final long serialVersionUID = -3531439484732724601L;
    private final String fullName;
    private final int amount;
    private final String phone;

    public UserPrincipal(String username, String password, String fullName, int amount, String phone, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.fullName = fullName;
        this.amount = amount;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAmount() {
        return amount;
    }

    public String getPhone() {
        return phone;
    }
}
