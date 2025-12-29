package com.alura.aluraflixapi.controller.video;


import com.alura.aluraflixapi.domain.video.dto.UpdateVideoDTO;
import com.alura.aluraflixapi.domain.video.dto.VideoDTO;
import com.alura.aluraflixapi.infraestructure.service.video.VideoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@SecurityRequirement(name = "bearer-key")
public class VideoController {

    private static final String LOGGING_PREFIX = "[VideoController]";

    private final VideoService service;

    @Autowired
    public VideoController(final VideoService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<VideoDTO>> getVideos(final Pageable pageable) {
        final Page<VideoDTO> videos = this.service.getVideos(pageable);
        return ResponseEntity.ok().body(new PageImpl<>(videos.getContent(), pageable, pageable.getPageSize()));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoDTO> getById(@NotBlank @PathVariable final String id) {
        return Optional.ofNullable(service.getById(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<VideoDTO> save(@Valid @RequestBody final VideoDTO dto,
                                         final UriComponentsBuilder uriBuilder) {
        log.info("{} Request to Save a new video: {}", LOGGING_PREFIX, dto);
        final VideoDTO videoDto = this.service.save(dto);
        //good practices to return the Location in the Header to be searched by ID
        //return Http code 201 and Location with ID
        return ResponseEntity.created(uriBuilder.path("/videos/{id}").buildAndExpand(videoDto.id())
                .toUri()).body(videoDto);
    }

    @PutMapping
    public ResponseEntity<UpdateVideoDTO> update(@Valid @RequestBody final UpdateVideoDTO dto,
                                                 final UriComponentsBuilder uriBuilder) {

        final var videoDto = this.service.updateMovie(dto);
        //good practices to return the Location in the Header to be searched by ID
        //return Http code 201 and Location with ID
        return ResponseEntity.created(uriBuilder.path("/videos/{id}")
                .buildAndExpand(videoDto.id())
                .toUri()).body(videoDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<VideoDTO> delete(@NotBlank @PathVariable final String id) {

        final var dto = this.service.delete(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/title")
    public ResponseEntity<List<VideoDTO>> getVideosByTitle(
            @NotBlank @RequestParam("title") final String title) {
        final var videosByTitle = this.service.getVideosByTitle(title);
        if (videosByTitle.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(videosByTitle);
        }

    }

}
