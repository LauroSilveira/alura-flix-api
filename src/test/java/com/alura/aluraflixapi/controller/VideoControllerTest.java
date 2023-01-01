package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.dto.VideoDto;
import com.alura.aluraflixapi.mapper.VideoMapper;
import com.alura.aluraflixapi.repository.VideoRepository;
import com.alura.aluraflixapi.service.VideoService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(SpringExtension.class)
@WebMvcTest
class VideoControllerTest {

  private static ObjectMapper mapper;

  @MockBean
  private VideoService service;

  @Autowired
  private MockMvc mockMvc;

  @SpyBean
  private VideoController videoController;

  @MockBean
  private VideoRepository videoRepository;

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

    Mockito.when(this.service.getVideos(Mockito.any()))
        .thenReturn(new PageImpl<>(videos));

    final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn();
    //When
    List<VideoDto> videosDtos = Arrays.asList(
        mapper.readValue(response.getResponse().getContentAsString(), VideoDto[].
            class));

    //Then
    Assertions.assertNotNull(videosDtos);
    Assertions.assertEquals(4, videosDtos.size());
  }

  @Test
  void save_a_new_video_test() throws Exception {

    //Given
    final VideoDto request = VideoDto.builder()
        .id(UUID.randomUUID().toString())
        .url("http://www.ringsofpower.com")
        .title("Rings of power")
        .description("Rings of power Amazon Series")
        .build();

    Mockito.when(this.service.save(Mockito.any()))
        .thenReturn(request);
    //When
    final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.post("/videos")
            .contentType(MediaType.APPLICATION_JSON)
            //necessary convert to bytes because his content Type JSON
            .content(mapper.writeValueAsBytes(request))
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn();

    //Then
    VideoDto dto = mapper.readValue(response.getResponse().getContentAsString(),
        VideoDto.class);

    Assertions.assertEquals(request, dto);
  }

  @Test
  void update_a_video_test() throws Exception {

    //Given
    final VideoDto videoToUpdate = buildVideosDto().get(2);
    Mockito.when(this.service.updatePut(Mockito.any()))
        .thenReturn(videoToUpdate);

    final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.put("/videos")
            .contentType(MediaType.APPLICATION_JSON)
            //necessary convert to bytes because his content Type JSON
            .content(mapper.writeValueAsBytes(videoToUpdate))
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn();

    //Then
    VideoDto videoUpdated = mapper.readValue(response.getResponse().getContentAsString(),
        VideoDto.class);

    Assertions.assertNotNull(videoUpdated);

  }

  @Test
  void delete_a_video_test() throws Exception {

    //Given
    Mockito.when(this.service.delete(Mockito.anyString()))
        .thenReturn(Optional.empty());
    //then
    this.mockMvc.perform(MockMvcRequestBuilders.delete("/videos/{id}", "1")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }


  private static List<VideoDto> buildVideosDto() {
    return List.of(VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .title("Lord of the rings - fellowship of the ring")
            .description("Lord of the rings - fellowship of the ring")
            .url("http://www.lordoftherings.com")
            .build(),
        VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .title("Lord of the rings - return of the king")
            .description("Lord of the rings - return of the king")
            .url("http://www.lordoftherings.com")
            .build(),
        VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .title("Lord of the rings - The Two towers")
            .description("Lord of the rings - The Two towers")
            .url("http://www.lordoftherings.com")
            .build(),
        VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .title("The hobbit - unnespect adventure")
            .description("The hobbit - unnespect adventure")
            .url("http://www.thehobbit.com")
            .build()
    );
  }
}