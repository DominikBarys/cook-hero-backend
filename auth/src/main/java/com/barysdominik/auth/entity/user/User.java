package com.barysdominik.auth.entity.user;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Setter
@Table
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private long id;
    private String uuid;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    private LocalDate joinedAt;
    private int amountOfCreatedTutorials;
    private int amountOfReviews;
    private boolean isLock;
    private boolean isEnabled;

    public User(
            long id,
            String uuid,
            String username,
            String password,
            Role role,
            Rank rank,
            LocalDate joinedAt,
            int amountOfCreatedTutorials,
            int amountOfReviews,
            boolean isLock,
            boolean isEnabled
    ) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.rank = rank;
        this.joinedAt = joinedAt;
        this.amountOfCreatedTutorials = amountOfCreatedTutorials;
        this.amountOfReviews = amountOfReviews;
        this.isLock = isLock;
        this.isEnabled = isEnabled;
        generateUuid();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //no need to implement mechanism that check if account is expired
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLock;
    }

    //no need to implement mechanism of expiration date of password
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    private void generateUuid() {
        if (uuid == null || uuid.equals("")) {
            setUuid(UUID.randomUUID().toString());
        }
    }
}
