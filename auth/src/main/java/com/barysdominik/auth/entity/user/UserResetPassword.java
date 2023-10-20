package com.barysdominik.auth.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_reset_password")
@NoArgsConstructor
@AllArgsConstructor
public class UserResetPassword {
    @Id
    @GeneratedValue(generator = "user_reset_password_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "user_reset_password_id_seq",
            sequenceName = "user_reset_password_id_seq",
            allocationSize = 1
    )
    private long id;
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    private Date createdAt;
}

