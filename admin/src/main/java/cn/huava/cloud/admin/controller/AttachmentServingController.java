package cn.huava.cloud.admin.controller;

import cn.huava.cloud.admin.mapper.AttachmentMapper;
import cn.huava.cloud.admin.pojo.po.AttachmentPo;
import cn.huava.cloud.admin.service.attachment.AceAttachmentService;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.common.controller.BaseController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * When visiting path like /20240824/985d124c52a38fb1985d124c52a38fb1.java , return the file
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/20{date:\\d{6}}/{filename:.+}")
public class AttachmentServingController
    extends BaseController<AceAttachmentService, AttachmentMapper, AttachmentPo> {

  @GetMapping
  public ResponseEntity<Resource> serveFile(
      @PathVariable @NonNull final String date, @PathVariable @NonNull final String filename) {
    String url = Fn.format("/20{}/{}", date, filename);
    return service.serveFile(url);
  }
}
