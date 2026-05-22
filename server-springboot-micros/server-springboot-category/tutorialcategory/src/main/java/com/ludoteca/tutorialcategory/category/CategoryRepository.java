package com.ludoteca.tutorialcategory.category;

import com.ludoteca.tutorialcategory.category.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
