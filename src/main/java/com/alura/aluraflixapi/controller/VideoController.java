package com.alura.aluraflixapi.controller;


import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDto;
import com.alura.aluraflixapi.domain.video.dto.VideoDto;
import com.alura.aluraflixapi.infraestructure.service.VideoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
  public ResponseEntity<Page<VideoDto>> getVideos(Pageable pageable) {
    final Page<VideoDto> videos = this.service.getVideos(pageable);
    if (videos.hasContent()) {
      return ResponseEntity.ok(videos);
    } else {
      return ResponseEntity.noContent().build();
    }
  }


  @GetMapping("/{id}")
  public ResponseEntity<VideoDto> getById(@NotBlank @PathVariable final String id) {
    return Optional.ofNullable(service.getById(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());

  }

  @PostMapping
  public ResponseEntity<VideoDto> save(@Valid @RequestBody final VideoDto dto,
      final UriComponentsBuilder uriBuilder) {
    final VideoDto videoDto = this.service.save(dto);
    //good practices to return the Location in the Header to be search by Id
    //return Http code 201 and Localtion with Id
    return ResponseEntity.created(uriBuilder.path("/videos/{id}").buildAndExpand(videoDto.id())
        .toUri()).body(videoDto);
  }

  @PutMapping
  public ResponseEntity<UpdateVideoDto> update(@Valid @RequestBody final UpdateVideoDto dto,
      final UriComponentsBuilder uriBuilder) {
    final var videoDto = this.service.updateMovie(dto);
    //good practices to return the Location in the Header to be search by Id
    //return Http code 201 and Localtion with Id
    return ResponseEntity.created(uriBuilder.path("/videos/{id}")
        .buildAndExpand(videoDto.id())
        .toUri()).body(videoDto);
  }

  @DeleteMapping("/{id}")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<VideoDto> delete(@NotBlank @PathVariable final String id) {
    final Optional<VideoDto> dto = this.service.delete(id);
    return dto.map(videoDto -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(videoDto))
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @GetMapping("/search")
  public ResponseEntity<List<VideoDto>> getVideosByTitle(
      @NotBlank @RequestParam("title") final String title) {
    final var videosByTitle = this.service.getVideosByTitle(title);
    if (videosByTitle.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.FOUND).body(videosByTitle);
    }

  }

}
