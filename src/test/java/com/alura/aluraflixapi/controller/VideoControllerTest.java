package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.category.CategoryController;
import com.alura.aluraflixapi.controller.user.AuthenticationController;
import com.alura.aluraflixapi.controller.user.UserController;
import com.alura.aluraflixapi.controller.video.VideoController;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.mapper.VideoMapper;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.repository.VideoRepository;
import com.alura.aluraflixapi.infraestructure.security.SecurityFilter;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.service.category.CategoryService;
import com.alura.aluraflixapi.infraestructure.service.user.UserService;
import com.alura.aluraflixapi.infraestructure.service.video.VideoServiceImpl;
import com.alura.aluraflixapi.jsonutils.ParseJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
//@WebMvcTest: Includes both the @AutoConfigureWebMvc and the @AutoConfigureMockMvc, among other functionality.
@WebMvcTest
//this annotation can be replaced at each test method scope
@WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
class VideoControllerTest extends ParseJson {

    private static final String PREFIX_PATH = "/video/";

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
    @DisplayName("Should return all videos and response 200 OK")
    void get_all_videos_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getAllVideos_response_ok.json");
        final var videosExpect = Arrays.stream(parseToJavaObject(jsonFile, VideoDto[].class)).toList();
        //Workaround to fix JsonSerialize on Spring boot version 3.2.0
        final var pageable = PageRequest.of(0, 10);
        when(this.videoService.getVideos(Mockito.any()))
                .thenReturn(new PageImpl<>(videosExpect, pageable, videosExpect.size()));

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
        assertEquals(videosExpect.size(), videosDtos.size());
    }

    @Test
    @DisplayName("Should return a video by Id and response 200 OK")
    void get_video_by_id() throws Exception {

        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getById_video_response_ok.json");
        final var videoDto = parseToJavaObject(jsonFile, VideoDto.class);
        when(this.videoService.getById(Mockito.anyString()))
                .thenReturn(videoDto);

        //When
        final MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos/{id}", "63680c011892283477b3e9b9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        VideoDto response = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                VideoDto.class);

        assertNotNull(videoDto);
        assertAll(() -> assertEquals(response.id(), videoDto.id()),
                () -> Assertions.assertEquals(response.url(), videoDto.url()),
                () -> Assertions.assertEquals(response.description(), videoDto.description()),
                () -> Assertions.assertEquals(response.title(), videoDto.title())
        );
    }

    @Test
    @DisplayName("Should create a new Video and response 200 OK")
    void save_a_new_video_test() throws Exception {

        //Given
        final VideoDto request = new VideoDto(UUID.randomUUID().toString(), "Rings of power Amazon Series", "Rings of power", "http://www.ringsofpower.com",
                new CategoryDto(UUID.randomUUID().toString(), Rating.FANTASY.name(), "Fantasy", "#ffd700"));

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
    @DisplayName("Should update a video by Id and return 200 OK")
    void update_video_by_id_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getById_video_response_ok.json");
        final var videoToUpdate = parseToJavaObject(jsonFile, UpdateVideoDto.class);

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
    @DisplayName("Should allow delete a video by id and response 200 OK")
    void delete_video_by_id_test() throws Exception {

        //Given
        final var videoDto = new VideoDto("63680c011892283477b3e9b9", "63680c011892283477b3e9b9", "best movie ever", "https://lordoftherings.com",
                new CategoryDto("63f67ec16295ed744dd460cd", "FREE", "Fantasy", "#ffff83"));
        when(this.videoService.delete(Mockito.anyString()))
                .thenReturn(videoDto);
        //then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/videos/{id}", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                ).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Should return a list of videos searched by title response OK")
    void getVideosByTitle_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getVideoByTitle_response_ok.json");
        final var videosByTitleExpected = Arrays.stream(parseToJavaObject(jsonFile, VideoDto[].class)).toList();
        when(this.videoService.getVideosByTitle(Mockito.anyString()))
                .thenReturn(videosByTitleExpected);

        final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos/title")
                        .param("title", "The Hobbit - The battle of five armies")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();

        //Then
        assertNotNull(response);
        final var videosByTitleResponse = Arrays.stream(mapper.readValue(response.getResponse().getContentAsString(),
                VideoDto[].class)).toList();
        org.assertj.core.api.Assertions.assertThat(videosByTitleResponse).usingRecursiveComparison()
                .isEqualTo(videosByTitleExpected);
    }

    @Test
    @DisplayName("Should not return a list of videos searched by title response No Content")
    void getVideosByTitle_response_no_content_test() throws Exception {
        //Given
        when(this.videoService.getVideosByTitle(Mockito.anyString()))
                .thenReturn(List.of());

        final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos/title")
                        .param("title", "The Hobbit - The battle of five armies")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}