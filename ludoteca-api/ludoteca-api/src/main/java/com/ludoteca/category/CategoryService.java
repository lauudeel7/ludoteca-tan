package com.ludoteca.category;

import com.ludoteca.category.model.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> findAll();

    CategoryDTO save(CategoryDTO category);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);
}
