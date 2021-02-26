package com.myfilmrental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "film")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @Id
    @Column(name = "film_id", length = 10)
    private String filmId;

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT NOT NULL")
    private String description;

    @Column(name = "release_year", nullable = false, columnDefinition = "INT(4)")
    private int releaseYear;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "film_actor", joinColumns = { @JoinColumn(name = "film_id") },
            inverseJoinColumns = {@JoinColumn(name = "actor_id") })
    private Set<Actor> actors;

    public Film(String filmId, String title, String description, int releaseYear, Language language, Country country) {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.country = country;
    }

    public Film(String filmId) {
        this.filmId = filmId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filmId == null) ? 0 : filmId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Film other = (Film) obj;
        if (filmId == null) {
            if (other.filmId != null)
                return false;
        } else if (!filmId.equals(other.filmId))
            return false;
        return true;
    }

}
