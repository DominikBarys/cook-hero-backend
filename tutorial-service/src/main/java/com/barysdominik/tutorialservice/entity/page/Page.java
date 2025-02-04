package com.barysdominik.tutorialservice.entity.page;

import com.barysdominik.tutorialservice.entity.tutorial.Tutorial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Table(name = "page")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    @Id
    @GeneratedValue(generator = "page_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "page_id_seq", sequenceName = "page_id_seq", allocationSize = 1)
    private long id;
    private String shortId;
    private int pageNumber;
    @Column(columnDefinition = "varchar(1048576)")
    private String htmlContent;
    @ManyToOne
    @JoinColumn(name = "tutorialId", referencedColumnName = "id")
    private Tutorial tutorial;
}
