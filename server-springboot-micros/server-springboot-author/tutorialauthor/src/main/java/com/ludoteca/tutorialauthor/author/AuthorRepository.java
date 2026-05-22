package com.ludoteca.tutorialauthor.author;

import com.ludoteca.tutorialauthor.author.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    /**
     * Método para recuperar un listado paginado de {@link Author}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Author}
     */
    Page<Author> findAll(Pageable pageable);

    Optional<Author> findByName(String name);

}
