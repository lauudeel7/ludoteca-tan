package com.ludoteca.category;

import com.ludoteca.category.model.Category;
import com.ludoteca.category.model.CategoryDTO;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<CategoryDTO> findAll() {

        return categoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    public CategoryDTO save(CategoryDTO dto) {
        if (dto.getName() != null) {
            categoryRepository.findByNameIgnoreCase(dto.getName().trim()).ifPresent(c -> {
                throw new BadRequestException("CATEGORY_NAME_EXISTS");
            });
        }

        Category category = new Category();
        category.setName(dto.getName() != null ? dto.getName().trim() : null);
        return mapToDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(dto.getName());
        return mapToDTO(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        boolean hasGames = gameRepository.existsByCategoryId(id);

        if (hasGames) {
            throw new BadRequestException("No se puede eliminar la categoría. Tiene juegos asociados.");
        }

        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
}
