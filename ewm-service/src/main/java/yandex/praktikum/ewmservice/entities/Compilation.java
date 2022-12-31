package yandex.praktikum.ewmservice.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Builder(setterPrefix = "with")
@Table(name = "compilations")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "events_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    @NotNull
    private List<Event> events;
    @Column(name = "pinned", nullable = false)
    @NotNull
    private Boolean pinned;
    @Column(name = "title", nullable = false)
    @NotBlank
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Compilation that = (Compilation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
