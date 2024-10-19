package cn.huava.cloud.member.service.captcha;

import cn.huava.cloud.common.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.swing.captcha.*;
import org.springframework.stereotype.Service;

/**
 * 1. If it's not GraalVM native image mode, use local dynamic captcha (use java.awt to generate
 * every time). <br>
 * 2. If it is GraalVM native image mode, try to use the online captcha API first. <br>
 * 3. If step 2 fails, use the local static captcha (the static images that have already been
 * generated). <br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class RefreshCaptchaService {
  protected void refresh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    LineCaptcha captcha = CaptchaUtil.ofLineCaptcha(100, 40, 4, 4);
    req.getSession().setAttribute(CommonConstant.CAPTCHA_CODE_SESSION_KEY, captcha.getCode());
    writeResponse(resp, captcha.getImageBytes());
  }

  private static void writeResponse(HttpServletResponse resp, byte[] imageBytes)
      throws IOException {
    resp.setContentType("image/jpeg");
    resp.getOutputStream().write(imageBytes);
    resp.getOutputStream().flush();
  }
}
