package com.alura.aluraflixapi.controller;


import com.alura.aluraflixapi.dto.VideoDto;
import com.alura.aluraflixapi.service.VideoService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/videos")
public class VideoController {

  private final VideoService service;

  @Autowired
  public VideoController(final VideoService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<VideoDto>> getVideos() {
    return Optional.ofNullable(service.getVideos())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }


  @GetMapping("/{id}")
  public ResponseEntity<VideoDto> getById(@NotBlank @PathVariable final String id) {
    return Optional.ofNullable(service.getById(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<VideoDto> save(@Valid @RequestBody final VideoDto dto) {
    final VideoDto videoDto = service.save(dto);
    return ResponseEntity.ok(videoDto);
  }

  @PutMapping
  public ResponseEntity<VideoDto> updatePut(@Valid @RequestBody final VideoDto dto) {
    final Optional<VideoDto> videoDto = Optional.ofNullable(service.updatePut(dto));
    return videoDto.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.internalServerError().build());
  }

  @DeleteMapping
  public ResponseEntity<VideoDto> delete(@NotBlank @PathVariable final String id) {
    final Optional<VideoDto> dto = service.delete(id);
    return dto.map(videoDto -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(videoDto))
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

}
