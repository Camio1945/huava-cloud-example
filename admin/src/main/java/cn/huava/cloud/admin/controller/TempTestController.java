package cn.huava.cloud.admin.controller;

import cn.huava.cloud.admin.service.captcha.AceCaptchaService;
import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.admin.cache.UserCache;
import cn.huava.cloud.admin.service.user.AceUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Temporary test controller, all code inside will be deleted at any time.
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/temp/test")
public class TempTestController {
  private final AceCaptchaService captchaService;
  private final AceUserService userService;
  private final UserCache userCache;

  @GetMapping("/echo/{value}")
  public String echo(@PathVariable String value) {
    return value;
  }

  @GetMapping("/hello")
  public String hello() {
    long start = System.nanoTime();
    Thread.ofPlatform().start(() -> {});
    long platformTime = System.nanoTime() - start;

    start = System.nanoTime();
    Thread.ofVirtual().start(() -> {});
    long virtualTime = System.nanoTime() - start;
    return "platformTime : " + platformTime + "\n<br>virtualTime : " + virtualTime;
  }

  @GetMapping("/")
  public Object test(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    return RedisUtil.getHitRatioPercentage();
  }

  @PostMapping("/")
  public Object testPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    return RedisUtil.getHitRatioPercentage();
  }
}
