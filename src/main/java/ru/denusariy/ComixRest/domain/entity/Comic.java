package ru.denusariy.ComixRest.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comic")
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    @NotBlank
    private String title;
    @Column(name = "year")
    @Min(value = 1900)
    private int year;
    @Column(name = "writer")
    @NotBlank
    private String writer;
    @Column(name = "artist")
    @NotBlank
    private String artist;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "book_id")
    private Book book;
}
