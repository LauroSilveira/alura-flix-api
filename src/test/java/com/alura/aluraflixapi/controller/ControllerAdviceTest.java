package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.dto.ErrorVO;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.exception.ErrorMessageVO;
import com.alura.aluraflixapi.infraestructure.exception.ResourceNotFoundException;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.service.CategoryService;
import com.alura.aluraflixapi.infraestructure.service.UserService;
import com.alura.aluraflixapi.infraestructure.service.VideoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
class ControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    @MockBean
    private AuthenticationController authenticationController;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

    @MockBean
    private VideoService videoService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository UserRepository;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return a ErrorDto with the missing fields and response status 400")
    void handleInvalidFields_test() throws Exception {
        //Given
        final var request = new VideoDto(UUID.randomUUID().toString(), "Lord of the rings",
                "The lord of the rings - The Fellowship of the ring", "www.thelordoftherings.com", null);
        //When
        final var response = this.mockMvc.perform(MockMvcRequestBuilders.post("/videos")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //Then
        final var errors = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<ErrorVO>>() {
        });
        assertThat(errors)
                .hasSize(2)
                .isNotNull();

    }

    @Test
    void handlerResourceNotFoundException_test() throws Exception {
        when(this.videoService.getById(anyString()))
                .thenThrow(new ResourceNotFoundException("Resource not found for id: 1"));
        //When
        final var response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos/{id}", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        //Then
        final var errorMessageVO = objectMapper.readValue(response.getResponse().getContentAsString(), ErrorMessageVO.class);
        Assertions.assertThat(errorMessageVO.message())
                .isNotNull()
                .isEqualTo("Resource not found for id: 1");

    }
}