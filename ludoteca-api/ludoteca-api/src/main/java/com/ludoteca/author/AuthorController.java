package com.ludoteca.author;

import com.ludoteca.author.model.AuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@CrossOrigin(origins = "*")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public Page<AuthorDTO> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return authorService.findAll(page, size);
    }

    @PostMapping
    public AuthorDTO create(@RequestBody AuthorDTO dto) {
        return authorService.save(dto);
    }

    @PutMapping(value = { "", "/{id}" })
    public AuthorDTO update(@PathVariable(required = false) Long id, @RequestBody AuthorDTO dto) {
        Long targetId = (id != null) ? id : dto.getId();

        if (targetId == null) {
            return authorService.save(dto);
        }

        return authorService.update(targetId, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
