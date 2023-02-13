package ru.denusariy.ComixRest.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import ru.denusariy.ComixRest.domain.enums.Format;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@DynamicUpdate
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {
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
    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private Format format;
    @Column(name = "alt_cover")
    private boolean isAltCover;
    @Column(name = "autograph")
    private boolean isAutograph;
    @Column(name = "signature")
    private String signature;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comic> comics;
}
