package com.ludoteca.author;

import com.ludoteca.author.model.Author;
import com.ludoteca.author.model.AuthorDto;
import com.ludoteca.author.model.AuthorSearchDto;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    GameRepository gameRepository;

    /**
     * {@inheritDoc}
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

        Author author;

        if (id == null) {
            author = new Author();
        } else {
            author = this.authorRepository.findById(id).orElse(null);
        }

        BeanUtils.copyProperties(data, author, "id");

        this.authorRepository.save(author);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws BadRequestException {

        if (!this.authorRepository.existsById(id)) {
            throw new BadRequestException("El autor seleccionado no existe o ya ha sido eliminado.");
        }

        if (this.gameRepository.existsByAuthorId(id)) {
            throw new BadRequestException("No se puede eliminar el autor porque tiene juegos asociados en el catálogo.");
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
