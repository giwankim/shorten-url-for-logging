package kr.co.shortenurlservice.presentation;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingFilter implements Filter {
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (servletRequest instanceof HttpServletRequest httpServletRequest) {
      // 요청을 CachedBodyHttpServletRequest로 래핑하여 바디를 캐시
      CachedBodyHttpServletRequest wrappedRequest =
          new CachedBodyHttpServletRequest(httpServletRequest);

      // 요청 URL, 메서드, 바디를 로깅
      String url = wrappedRequest.getRequestURI();
      String method = wrappedRequest.getMethod();
      String body = wrappedRequest.getReader().lines().reduce("", String::concat);

      log.trace("Incoming Request: URL={}, Method={}, Body={}", url, method, body);

      // 래핑된 요청 객체를 다음 필터 체인으로 전달
      filterChain.doFilter(wrappedRequest, servletResponse);
    } else {
      // HttpServletRequest가 아닌 경우, 원래 요청을 그대로 전달/사용
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }
}
