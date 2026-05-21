package com.ludoteca.author;

import com.ludoteca.author.model.Author;
import com.ludoteca.author.model.AuthorDTO;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Page<AuthorDTO> findAll(int page, int size) {
        return authorRepository.findAll(PageRequest.of(page, size)).map(this::mapToDTO);
    }

    @Override
    public AuthorDTO save(AuthorDTO dto) {
        if (authorRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Ya existe un autor registrado con ese nombre.");
        }
        Author author = new Author();
        author.setName(dto.getName());
        author.setNationality(dto.getNationality());

        return mapToDTO(authorRepository.save(author));
    }

    @Override
    public AuthorDTO update(Long id, AuthorDTO dto) {
        Author author = authorRepository.findById(id).orElseThrow();

        if (!author.getName().equalsIgnoreCase(dto.getName()) && authorRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Ya existe un autor registrado con ese nombre.");
        }

        author.setName(dto.getName());
        author.setNationality(dto.getNationality());

        return mapToDTO(authorRepository.save(author));
    }

    @Override
    public void delete(Long id) {
        boolean hasGames = gameRepository.existsByAuthorId(id);

        if (hasGames) {
            throw new BadRequestException("No se puede eliminar el autor. Tiene juegos asociados.");
        }

        authorRepository.deleteById(id);
    }

    private AuthorDTO mapToDTO(Author a) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(a.getId());
        dto.setName(a.getName());
        dto.setNationality(a.getNationality());
        return dto;
    }
}
