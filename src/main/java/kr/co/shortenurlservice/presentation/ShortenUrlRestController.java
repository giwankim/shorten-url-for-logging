package kr.co.shortenurlservice.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import kr.co.shortenurlservice.application.SimpleShortenUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShortenUrlRestController {
  private final SimpleShortenUrlService simpleShortenUrlService;

  @PostMapping(value = "/shortenUrl")
  public ResponseEntity<ShortenUrlCreateResponseDto> createShortenUrl(
      @Valid @RequestBody ShortenUrlCreateRequestDto shortenUrlCreateRequestDto) {
    ShortenUrlCreateResponseDto shortenUrlCreateResponseDto =
        simpleShortenUrlService.generateShortenUrl(shortenUrlCreateRequestDto);
    return ResponseEntity.ok(shortenUrlCreateResponseDto);
  }

  @GetMapping(value = "/{shortenUrlKey}")
  public ResponseEntity<?> redirectShortenUrl(@PathVariable String shortenUrlKey)
      throws URISyntaxException {
    String originalUrl = simpleShortenUrlService.getOriginalUrlByShortenUrlKey(shortenUrlKey);

    URI redirectUri = new URI(originalUrl);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(redirectUri);

    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping(value = "/shortenUrl/{shortenUrlKey}")
  public ResponseEntity<ShortenUrlInformationDto> getShortenUrlInformation(
      @PathVariable String shortenUrlKey) {
    ShortenUrlInformationDto shortenUrlInformationDto =
        simpleShortenUrlService.getShortenUrlInformationByShortenUrlKey(shortenUrlKey);
    return ResponseEntity.ok(shortenUrlInformationDto);
  }

  @GetMapping(value = "/shortenUrl")
  public ResponseEntity<List<ShortenUrlInformationDto>> getAllShortenUrlInformation() {
    List<ShortenUrlInformationDto> shortenUrlInformationDtoList =
        simpleShortenUrlService.getAllShortenUrlInformationDto();

    return ResponseEntity.ok(shortenUrlInformationDtoList);
  }
}
