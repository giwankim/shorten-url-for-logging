package kr.co.shortenurlservice.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import kr.co.shortenurlservice.application.SimpleShortenUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ShortenUrlRestController {
  private final SimpleShortenUrlService simpleShortenUrlService;

  @PostMapping(value = "/shortenUrl")
  public ShortenUrlCreateResponseDto createShortenUrl(
      @Valid @RequestBody ShortenUrlCreateRequestDto shortenUrlCreateRequestDto) {
    return simpleShortenUrlService.generateShortenUrl(shortenUrlCreateRequestDto);
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
  public ShortenUrlInformationDto getShortenUrlInformation(@PathVariable String shortenUrlKey) {
    return simpleShortenUrlService.getShortenUrlInformationByShortenUrlKey(shortenUrlKey);
  }

  @GetMapping(value = "/shortenUrl")
  public List<ShortenUrlInformationDto> getAllShortenUrlInformation() {
    return simpleShortenUrlService.getAllShortenUrlInformationDto();
  }
}
