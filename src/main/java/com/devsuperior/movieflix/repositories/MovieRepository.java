package com.devsuperior.movieflix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.movieflix.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = "genre")
    @Query(value = """
        FROM Movie m
        WHERE :genreIds IS NULL OR m.genre.id IN :genreIds
    """)
    Page<Movie> findAllByGenre(Pageable pageable, @Param(value = "genreIds") List<Long> genreIds);

    @EntityGraph(attributePaths = { "genre", "reviews", "reviews.user" })
    @Query(value = """
        FROM Movie m
        WHERE m.id = :id        
    """)
    Optional<Movie> findByIdWithReviews(Long id);
    
}
