package com.ludoteca.author;

import com.ludoteca.author.model.AuthorDTO;
import org.springframework.data.domain.Page;

public interface AuthorService {

    Page<AuthorDTO> findAll(int page, int size);

    AuthorDTO save(AuthorDTO dto);

    AuthorDTO update(Long id, AuthorDTO dto);

    void delete(Long id);
}
