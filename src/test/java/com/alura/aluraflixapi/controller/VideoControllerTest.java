package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.authentication.AuthenticationController;
import com.alura.aluraflixapi.controller.category.CategoryController;
import com.alura.aluraflixapi.domain.category.Rating;
import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDTO;
import com.alura.aluraflixapi.domain.video.dto.VideoDTO;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.service.user.UserService;
import com.alura.aluraflixapi.infraestructure.service.video.VideoServiceImpl;
import com.alura.aluraflixapi.jsonutils.ParseJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockitoBean
    private AuthenticationController authenticationController;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private VideoServiceImpl videoService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CategoryController categoryController;


    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return all videos and response 200 OK")
    void get_all_videos_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getAllVideos_response_ok.json");
        final var videosExpect = Arrays.stream(parseToJavaObject(jsonFile, VideoDTO[].class)).toList();

        when(this.videoService.getVideos(Mockito.any()))
                .thenReturn(new PageImpl<>(videosExpect, PageRequest.of(0, 10), videosExpect.size()));

        final var response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos")
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //When
        JsonNode jsonNode = mapper.readTree(response.getResponse().getContentAsString()).get("content");
        List<VideoDTO> videosDtos = Arrays.asList(mapper.readValue(jsonNode.toString(), VideoDTO[].
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
        final var videoDto = parseToJavaObject(jsonFile, VideoDTO.class);

        when(this.videoService.getById(Mockito.anyString())).thenReturn(videoDto);

        //When
        final MvcResult mvcResult = this.mockMvc.perform(get("/videos/{id}", "63680c011892283477b3e9b9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //Then
        VideoDTO response = mapper.readValue(mvcResult.getResponse().getContentAsString(), VideoDTO.class);

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
        final VideoDTO request = new VideoDTO(
                UUID.randomUUID().toString(),
                "Rings of power Amazon Series",
                "Rings of power",
                "http://www.ringsofpower.com",
                new CategoryDto(UUID.randomUUID().toString(), Rating.FANTASY.name(),
                        "Fantasy",
                        "#ffd700"));

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
        VideoDTO dto = mapper.readValue(response.getResponse().getContentAsString(),
                VideoDTO.class);

        assertEquals(request, dto);
    }

    @Test
    @DisplayName("Should update a video by Id and return 200 OK")
    void update_video_by_id_test() throws Exception {
        //Given
        final var jsonFile = getJsonFile(PREFIX_PATH + "getById_video_response_ok.json");
        final var videoToUpdate = parseToJavaObject(jsonFile, UpdateVideoDTO.class);

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
        VideoDTO videoUpdated = mapper.readValue(response.getResponse().getContentAsString(),
                VideoDTO.class);

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
        final var videoDto = new VideoDTO("63680c011892283477b3e9b9", "63680c011892283477b3e9b9", "best movie ever", "https://lordoftherings.com",
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
        final var videosByTitleExpected = Arrays.stream(parseToJavaObject(jsonFile, VideoDTO[].class)).toList();

        when(this.videoService.getVideosByTitle(Mockito.anyString())).thenReturn(videosByTitleExpected);

        final MvcResult response = this.mockMvc.perform(get("/videos/title")
                        .param("title", "The Hobbit - The battle of five armies")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();

        //Then
        assertNotNull(response);
        final var videosByTitleResponse = Arrays.stream(mapper.readValue(response.getResponse().getContentAsString(),
                VideoDTO[].class)).toList();
        org.assertj.core.api.Assertions.assertThat(videosByTitleResponse).usingRecursiveComparison()
                .isEqualTo(videosByTitleExpected);
    }
}