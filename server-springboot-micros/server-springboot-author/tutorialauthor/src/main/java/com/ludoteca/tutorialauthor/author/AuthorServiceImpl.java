package com.ludoteca.tutorialauthor.author;

import com.ludoteca.tutorialauthor.author.model.Author;
import com.ludoteca.tutorialauthor.author.model.AuthorDto;
import com.ludoteca.tutorialauthor.author.model.AuthorSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Author get(Long id) {

        return this.authorRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findPage(AuthorSearchDto dto) {

        return this.authorRepository.findAll(dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, AuthorDto data) {
        Optional<Author> existingAuthor = this.authorRepository.findByName(data.getName());

        if (existingAuthor.isPresent()) {
            if (id == null || !existingAuthor.get().getId().equals(id)) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "El autor con el nombre '" + data.getName() + "' ya existe.");
            }
        }

        Author author;

        if (id == null) {
            author = new Author();
        } else {
            author = this.get(id);
        }

        BeanUtils.copyProperties(data, author, "id");

        this.authorRepository.save(author);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {

        if (this.get(id) == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not exists");
        }

        String url = "http://localhost:8080/game/exists-author/" + id;
        Boolean hasGames = new org.springframework.web.client.RestTemplate().getForObject(url, Boolean.class);

        if (Boolean.TRUE.equals(hasGames)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "El autor tiene juegos asociados");
        }

        this.authorRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> findAll() {

        return (List<Author>) this.authorRepository.findAll();
    }

}
