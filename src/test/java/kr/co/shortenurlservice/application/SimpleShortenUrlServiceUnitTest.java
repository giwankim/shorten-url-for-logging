package kr.co.shortenurlservice.application;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import kr.co.shortenurlservice.domain.LackOfShortenUrlKeyException;
import kr.co.shortenurlservice.domain.ShortenUrl;
import kr.co.shortenurlservice.domain.ShortenUrlRepository;
import kr.co.shortenurlservice.presentation.ShortenUrlCreateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleShortenUrlServiceUnitTest {
  @Mock private ShortenUrlRepository shortenUrlRepository;
  @InjectMocks private SimpleShortenUrlService simpleShortenUrlService;

  @Test
  @DisplayName("단축 URL이 계속 중복되면 LackOfShortenUrlKeyException 예외가 발생해야한다.")
  void throwLackOfShortenUrlKeyExceptionTest() {
    ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = new ShortenUrlCreateRequestDto(null);

    when(shortenUrlRepository.findShortenUrlByShortenUrlKey(any()))
        .thenReturn(new ShortenUrl(null, null));

    assertThatExceptionOfType(LackOfShortenUrlKeyException.class)
        .isThrownBy(() -> simpleShortenUrlService.generateShortenUrl(shortenUrlCreateRequestDto));
  }
}
