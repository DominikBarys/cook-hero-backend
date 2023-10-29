package com.barysdominik.auth.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Table(name = "users")
@Entity
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private long id;
    private String uuid;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    private LocalDate joinedAt;
    private int amountOfCreatedTutorials;
    @Column(name = "islock", columnDefinition = "boolean default true")
    private boolean isLock;
    @Column(name = "isenabled", columnDefinition = "boolean default false")
    private boolean isEnabled;

    public User(){
        generateUuid();
    }

    public User(
            String username,
            String password,
            String email,
            Role role,
            Rank rank,
            LocalDate joinedAt,
            int amountOfCreatedTutorials,
            boolean isLock,
            boolean isEnabled
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.rank = rank;
        this.joinedAt = joinedAt;
        this.amountOfCreatedTutorials = amountOfCreatedTutorials;
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
        return isEnabled;
    }

    private void generateUuid() {
        if (uuid == null || uuid.equals("")) {
            setUuid(UUID.randomUUID().toString());
        }
    }
}
