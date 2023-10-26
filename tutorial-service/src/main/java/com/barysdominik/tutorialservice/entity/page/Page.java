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
    @Column(nullable = false)
    private int pageNumber;
    @Column(nullable = false)
    //TODO mozliwe ze htmlContent bedzie tablica i bedzie druga tablica z adresami do zdjec
    // da to efekt tego ze pod danym kawalkiem html bedzie zdjecie itd.
    private String htmlContent;
    @ManyToOne
    @JoinColumn(name = "tutorialId", referencedColumnName = "id")
    private Tutorial tutorial;

    public Page(int pageNumber, String htmlContent, Tutorial tutorial) {
        this.pageNumber = pageNumber;
        this.htmlContent = htmlContent;
        this.tutorial = tutorial;
    }
}
