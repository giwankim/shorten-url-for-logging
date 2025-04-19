package kr.co.shortenurlservice.application;

import java.util.List;
import kr.co.shortenurlservice.domain.LackOfShortenUrlKeyException;
import kr.co.shortenurlservice.domain.NotFoundShortenUrlException;
import kr.co.shortenurlservice.domain.ShortenUrl;
import kr.co.shortenurlservice.domain.ShortenUrlRepository;
import kr.co.shortenurlservice.presentation.ShortenUrlCreateRequestDto;
import kr.co.shortenurlservice.presentation.ShortenUrlCreateResponseDto;
import kr.co.shortenurlservice.presentation.ShortenUrlInformationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimpleShortenUrlService {
  private final ShortenUrlRepository shortenUrlRepository;

  public ShortenUrlCreateResponseDto generateShortenUrl(
      ShortenUrlCreateRequestDto shortenUrlCreateRequestDto) {
    String originalUrl = shortenUrlCreateRequestDto.getOriginalUrl();
    String shortenUrlKey = getUniqueShortenUrlKey();
    log.debug("getUniqueShortenUrlKey {}", shortenUrlKey);

    ShortenUrl shortenUrl = new ShortenUrl(originalUrl, shortenUrlKey);
    shortenUrlRepository.saveShortenUrl(shortenUrl);
    log.info("shortenUrl 생성 {}", shortenUrl);

    return new ShortenUrlCreateResponseDto(shortenUrl);
  }

  public String getOriginalUrlByShortenUrlKey(String shortenUrlKey) {
    ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

    if (null == shortenUrl) {
      throw new NotFoundShortenUrlException("단축 URL을 찾지 못했습니다. shortenUrlKey=" + shortenUrlKey);
    }

    shortenUrl.increaseRedirectCount();
    shortenUrlRepository.saveShortenUrl(shortenUrl);

    return shortenUrl.getOriginalUrl();
  }

  public ShortenUrlInformationDto getShortenUrlInformationByShortenUrlKey(String shortenUrlKey) {
    ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

    if (null == shortenUrl) {
      throw new NotFoundShortenUrlException("단축 URL을 찾지 못했습니다. shortedUrlKey=" + shortenUrlKey);
    }

    return new ShortenUrlInformationDto(shortenUrl);
  }

  public List<ShortenUrlInformationDto> getAllShortenUrlInformationDto() {
    List<ShortenUrl> shortenUrls = shortenUrlRepository.findAll();

    return shortenUrls.stream().map(ShortenUrlInformationDto::new).toList();
  }

  private String getUniqueShortenUrlKey() {
    final int MAX_RETRY_COUNT = 5;
    int count = 0;

    while (count++ < MAX_RETRY_COUNT) {
      String shortenUrlKey = ShortenUrl.generateShortenUrlKey();
      ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

      if (null == shortenUrl) {
        return shortenUrlKey;
      }
    }

    throw new LackOfShortenUrlKeyException();
  }
}
