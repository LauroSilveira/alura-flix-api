package com.alura.aluraflixapi.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import com.alura.aluraflixapi.infraestructure.security.SecurityFilter;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.service.CategoryService;
import com.alura.aluraflixapi.infraestructure.service.UserService;
import com.alura.aluraflixapi.infraestructure.service.VideoServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(SpringExtension.class)
//@WebMvcTest: Includes both the @AutoConfigureWebMvc and the @AutoConfigureMockMvc, among other functionality.
@WebMvcTest
//this annotation can be replaced at each test method scope
@WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
class VideoControllerTest {

    private static ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private SecurityFilter securityFilter;

    @MockBean
    private UserService userService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private VideoServiceImpl videoService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationController authenticationController;

    @SpyBean
    private VideoController videoController;

    @MockBean
    private CategoryController categoryController;

    @MockBean
    private UserController userController;

    @MockBean
    private VideoRepository videoRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VideoMapper videoMapper;


    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void get_all_videos_test() throws Exception {
        //Given
        final List<VideoDto> videos = buildVideosDto();
        //Workaround to fix JsonSerialize on Spring boot version 3.2.0
        final var pageable = PageRequest.of(0, 10);
        when(this.videoService.getVideos(Mockito.any()))
                .thenReturn(new PageImpl<>(videos, pageable, videos.size()));

        final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //When
        JsonNode jsonNode = mapper.readTree(response.getResponse().getContentAsString()).get("content");
        List<VideoDto> videosDtos = Arrays.asList(mapper.readValue(jsonNode.toString(), VideoDto[].
                class));
        //Then
        assertNotNull(videosDtos);
        assertEquals(4, videosDtos.size());
    }

    @Test
    void get_video_by_id() throws Exception {

        //Given
        final VideoDto request = buildVideosDto().get(0);

        when(this.videoService.getById(Mockito.anyString()))
                .thenReturn(request);

        //When
        final MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        VideoDto videoDto = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                VideoDto.class);

        assertNotNull(videoDto);
        assertAll(() -> assertEquals(request.id(), videoDto.id()),
                () -> Assertions.assertEquals(request.url(), videoDto.url()),
                () -> Assertions.assertEquals(request.description(), videoDto.description()),
                () -> Assertions.assertEquals(request.title(), videoDto.title())
        );
    }

    @Test
    void save_a_new_video_test() throws Exception {

        //Given
        final VideoDto request = VideoDto.builder()
                .id(UUID.randomUUID().toString())
                .url("http://www.ringsofpower.com")
                .title("Rings of power")
                .description("Rings of power Amazon Series")
                .category(new CategoryDto(UUID.randomUUID().toString(), Rating.FANTASY.name(), "Fantasy",
                        "#ffd700"))
                .build();

        when(this.videoService.save(Mockito.any()))
                .thenReturn(request);

        //When
        final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.post("/videos")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        //necessary convert to bytes because his content Type JSON
                        .content(mapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        VideoDto dto = mapper.readValue(response.getResponse().getContentAsString(),
                VideoDto.class);

        assertEquals(request, dto);
    }

    @Test
    void update_video_by_id_test() throws Exception {

        //Given
        final var videoToUpdate =
                new UpdateVideoDto(UUID.randomUUID().toString(),
                        "Hobbit: La batalla de los cincos ejercitos", "La batalla de los cincos ejercitos",
                        "www.thehobbit2.com",
                        new CategoryDto(UUID.randomUUID().toString(), Rating.FANTASY.name(), "Fantasy",
                                "#FFD700"));

        when(this.videoService.updateMovie(Mockito.any()))
                .thenReturn(videoToUpdate);

        final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.put("/videos")
                        // When testing any non-safe HTTP methods and using Spring Security’s CSRF protection,
                        // you must include a valid CSRF Token in the request.
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        //necessary convert to bytes because his content Type JSON
                        .content(mapper.writeValueAsBytes(videoToUpdate))
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        VideoDto videoUpdated = mapper.readValue(response.getResponse().getContentAsString(),
                VideoDto.class);

        assertNotNull(videoUpdated);
        Assertions.assertAll(() ->
                        Assertions.assertEquals(videoToUpdate.id(), videoUpdated.id()),
                () -> Assertions.assertEquals(videoToUpdate.description(), videoUpdated.description()),
                () -> Assertions.assertEquals(videoToUpdate.url(), videoUpdated.url()),
                () -> Assertions.assertEquals(videoToUpdate.category(), videoUpdated.category())
        );

    }

    @Test
    void delete_video_by_id_test() throws Exception {

        //Given
        when(this.videoService.delete(Mockito.anyString()))
                .thenReturn(Optional.empty());
        //then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/videos/{id}", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                ).andDo(print())
                .andExpect(status().isNoContent());
    }


    private static List<VideoDto> buildVideosDto() {
        final var categoryDto = new CategoryDto(UUID.randomUUID().toString(), Rating.FREE.name(),
                "Fantasy", "#FFD700");
        return List.of(VideoDto.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Lord of the rings - fellowship of the ring")
                        .description("Lord of the rings - fellowship of the ring")
                        .url("http://www.lordoftherings.com")
                        .category(categoryDto)
                        .build(),
                VideoDto.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Lord of the rings - return of the king")
                        .description("Lord of the rings - return of the king")
                        .url("http://www.lordoftherings.com")
                        .category(categoryDto)
                        .build(),
                VideoDto.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Lord of the rings - The Two towers")
                        .description("Lord of the rings - The Two towers")
                        .url("http://www.lordoftherings.com")
                        .category(categoryDto)
                        .build(),
                VideoDto.builder()
                        .id(UUID.randomUUID().toString())
                        .title("The hobbit - unnespect adventure")
                        .description("The hobbit - unnespect adventure")
                        .url("http://www.thehobbit.com")
                        .category(categoryDto)
                        .build()
        );
    }
}