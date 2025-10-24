package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        return movieRepository.findById(id)
                .map(MovieDetailsDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findAllByGenre(Pageable pageable, List<Long> genreIds) {
        return movieRepository.findAllByGenre(
                    applyDefaultSort(pageable, "title"),
                    genreIds)
                .map(MovieCardDTO::new);
    }

    private Pageable applyDefaultSort(Pageable pageable, String sortField) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Optional.of(pageable.getSort())
                        .filter(Sort::isSorted)
                        .orElse(Sort.by(Sort.Direction.ASC, sortField)));
    }

}
