package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.authentication.AuthenticationController;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.service.category.CategoryService;
import com.alura.aluraflixapi.infraestructure.service.user.UserService;
import com.alura.aluraflixapi.infraestructure.service.video.VideoService;
import com.alura.aluraflixapi.jsonutils.ParseJson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
//@WebMvcTest: Includes both the @AutoConfigureWebMvc and the @AutoConfigureMockMvc, among other functionality.
@WebMvcTest
//this annotation can be replaced at each test method scope
@WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
class CategoryControllerTest extends ParseJson {

    private static final String PREFIX_PATH = "/category/";

    private static ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AuthenticationController authenticationController;

    @MockBean
    private UserService userService;

    @MockBean
    private VideoService videoService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return all Categories and response OK")
    void categories_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getAllCategories_response_ok.json");
        final var categoriesDtoExpected = Arrays.stream(parseToJavaObject(jsonFile, CategoryDto[].class)).toList();
        when(this.categoryService.categories())
                .thenReturn(categoriesDtoExpected);
        //When
        final var response = this.mockMvc.perform(MockMvcRequestBuilders.get("/category")
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        assertThat(response).isNotNull();
        final var categoriesResponse = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<CategoryDto>>() {
        });
        assertThat(categoriesDtoExpected).usingRecursiveComparison().isEqualTo(categoriesResponse);
    }

    @Test
    @DisplayName("Should create a new category and response OK")
    void create_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "create_category_response_ok.json");
        final var categoriesDtoExpected = parseToJavaObject(jsonFile, CategoryDto.class);
        when(this.categoryService.create(any()))
                .thenReturn(categoriesDtoExpected);
        //When
        final var response = this.mockMvc.perform(MockMvcRequestBuilders.post("/category")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .content(mapper.writeValueAsBytes(categoriesDtoExpected))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        assertThat(response).isNotNull();
        final var categoriesResponse = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<CategoryDto>() {
        });
        assertThat(categoriesDtoExpected).usingRecursiveComparison().isEqualTo(categoriesResponse);
    }

    @Test
    @DisplayName("Should find a category by Id and response OK")
    void findCategoryById_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "findCategoryById_response_ok.json");
        final var categoriesDtoExpected = parseToJavaObject(jsonFile, CategoryDto.class);
        when(this.categoryService.findCategoryById(anyString()))
                .thenReturn(categoriesDtoExpected);
        //When
        final var response = this.mockMvc.perform(MockMvcRequestBuilders.get("/category/{id}", "63f67ec16295ed744dd460cd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andReturn();

        //Then
        assertThat(response).isNotNull();
        final var categoriesResponse = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<CategoryDto>() {
        });
        assertThat(categoriesDtoExpected).usingRecursiveComparison().isEqualTo(categoriesResponse);
    }

    @Test
    @DisplayName("Should return a video by category Id and response OK")
    void getVideosByCategory_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getVideosByCategory_response_ok.json");
        final var categoriesDtoExpected = Arrays.stream(parseToJavaObject(jsonFile, VideoDto[].class)).toList();
        when(this.categoryService.getVideosByCategory(anyString()))
                .thenReturn(categoriesDtoExpected);
        //When
        final var response = this.mockMvc.perform(MockMvcRequestBuilders.get("/category/{rating}/videos", "FREE")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andReturn();

        //Then
        assertThat(response).isNotNull();
        final var categoriesResponse = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<VideoDto>>() {
        });
        assertThat(categoriesDtoExpected).usingRecursiveComparison().isEqualTo(categoriesResponse);
    }

    @Test
    @DisplayName("Should return no content when request to getVideosByCategory")
    void getVideosByCategory_return_no_content_test() throws Exception {
        //Given
        when(this.categoryService.getVideosByCategory(anyString()))
                .thenReturn(List.of());
        //Then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/category/{rating}/videos", "FREE")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_test() throws Exception {
        //Given
        //The
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", "63f67ec16295ed744dd460cd")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());
    }
}