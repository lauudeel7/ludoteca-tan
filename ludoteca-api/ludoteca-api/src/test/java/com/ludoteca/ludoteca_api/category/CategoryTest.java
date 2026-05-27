package com.ludoteca.ludoteca_api.category;

import com.ludoteca.category.CategoryRepository;
import com.ludoteca.category.CategoryServiceImpl;
import com.ludoteca.category.model.Category;
import com.ludoteca.category.model.CategoryDto;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void findAllShouldReturnAllCategoriesMappedToDTO() {
        List<Category> list = new ArrayList<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Eurogames");
        list.add(category);

        when(categoryRepository.findAll()).thenReturn(list);

        List<CategoryDto> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Eurogames", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void saveShouldCreateCategoryAndReturnDTO() {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Dados");

        Category savedCategory = new Category();
        savedCategory.setId(4L);
        savedCategory.setName("Dados");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDto result = categoryService.save(inputDto);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Dados", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void updateShouldModifyExistingCategoryAndReturnDTO() {
        Long id = 1L;
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Eurogames Modificado");

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("Eurogames");

        Category updatedCategory = new Category();
        updatedCategory.setId(id);
        updatedCategory.setName("Eurogames Modificado");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryDto result = categoryService.update(id, inputDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Eurogames Modificado", result.getName());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void deleteWithoutGamesShouldInvokeRepositoryDelete() {
        Long id = 2L;
        when(gameRepository.existsByCategoryId(id)).thenReturn(false);

        categoryService.delete(id);

        verify(gameRepository, times(1)).existsByCategoryId(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteWithGamesShouldThrowBadRequestException() {
        Long id = 1L;
        when(gameRepository.existsByCategoryId(id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            categoryService.delete(id);
        });

        verify(gameRepository, times(1)).existsByCategoryId(id);
        verify(categoryRepository, never()).deleteById(id);
    }
}
