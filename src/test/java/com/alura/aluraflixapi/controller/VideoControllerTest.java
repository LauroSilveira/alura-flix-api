package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.dto.VideoDto;
import com.alura.aluraflixapi.mapper.VideoMapper;
import com.alura.aluraflixapi.repository.VideoRepository;
import com.alura.aluraflixapi.service.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  @MockBean
  private VideoService service;

  @Autowired
  private MockMvc mockMvc;

  @SpyBean
  private VideoController videoController;

  @MockBean
  private VideoRepository videoRepository;

  private static ObjectMapper mapper;


  @MockBean
  private VideoMapper videoMapper;

  @BeforeAll
  static void setup() {
    mapper = new ObjectMapper();
  }

  @Test
  void getVideosTest() throws Exception {
    //Given
    final List<VideoDto> videos = buildVideosDto();

   Mockito.when(this.service.getVideos(Mockito.any()))
       .thenReturn(new PageImpl<>(videos));

    final MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.get("/videos")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn();

    List<VideoDto> videosDtos= Arrays.asList(mapper.readValue(response.getResponse().getContentAsString(), VideoDto[].
        class));

    Assertions.assertNotNull(videosDtos);
    Assertions.assertEquals( 4, videosDtos.size());
  }

  private static List<VideoDto> buildVideosDto() {
    return List.of(VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .description("Lord of the rings - fellowship of the ring")
            .url("http://www.lordoftherings.com")
            .build(),
        VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .description("Lord of the rings - return of the king")
            .url("http://www.lordoftherings.com")
            .build(),
        VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .description("Lord of the rings - The Two towers")
            .url("http://www.lordoftherings.com")
            .build(),
        VideoDto.builder()
            .id(UUID.randomUUID().toString())
            .description("The hobbit - unnespect adventure")
            .url("http://www.thehobbit.com")
            .build()
    );
  }

  @Test
  void save() {
  }

  @Test
  void updatePut() {
  }

  @Test
  void delete() {
  }
}