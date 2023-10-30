package com.barysdominik.fileservice.entity.image;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(generator = "image_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "image_id_seq", sequenceName = "image_id_seq")
    private long id;
    private String shortId;
    @Column(columnDefinition = "varchar(1024)")
    private String path;
    private boolean isUsed;
    private LocalDate createdAt;
}
