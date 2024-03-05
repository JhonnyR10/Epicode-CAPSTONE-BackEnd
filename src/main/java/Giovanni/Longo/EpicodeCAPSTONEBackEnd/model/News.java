package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "news")
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_news")
    private Long id;

    private String title;
    private int readingTimeMinutes;
    private LocalDate creationDate;
    private String coverImageLink;
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
