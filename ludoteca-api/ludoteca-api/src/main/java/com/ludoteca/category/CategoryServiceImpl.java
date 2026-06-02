package com.ludoteca.category;

import com.ludoteca.category.model.Category;
import com.ludoteca.category.model.CategoryDto;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GameRepository gameRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Category get(Long id) {

        return this.categoryRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> findAll() {

        return (List<Category>) this.categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, CategoryDto dto) {

        Category category;

        if (id == null) {
            category = new Category();
        } else {
            category = this.categoryRepository.findById(id).orElse(null);
        }

        category.setName(dto.getName());

        this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws BadRequestException {

        if (!this.categoryRepository.existsById(id)) {
            throw new BadRequestException("La categoría seleccionada no existe o ya ha sido eliminada.");
        }

        if (this.gameRepository.existsByCategoryId(id)) {
            throw new BadRequestException("No se puede eliminar la categoría porque tiene juegos asociados en el catálogo.");
        }

        this.categoryRepository.deleteById(id);
    }

}
