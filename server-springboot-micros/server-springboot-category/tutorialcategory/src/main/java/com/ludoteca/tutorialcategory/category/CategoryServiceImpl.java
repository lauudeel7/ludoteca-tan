package com.ludoteca.tutorialcategory.category;

import com.ludoteca.tutorialcategory.category.model.Category;
import com.ludoteca.tutorialcategory.category.model.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

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
            category = this.get(id);
        }

        category.setName(dto.getName());

        this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        String url = "http://localhost:8080/game/exists-category/" + id;
        Boolean hasGames = new org.springframework.web.client.RestTemplate().getForObject(url, Boolean.class);

        if (Boolean.TRUE.equals(hasGames)) {
            throw new org.springframework.web.srver.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        this.categoryRepository.deleteById(id);
    }

}
