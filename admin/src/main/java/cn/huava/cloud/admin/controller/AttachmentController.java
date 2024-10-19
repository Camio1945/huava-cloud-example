package cn.huava.cloud.admin.controller;

import cn.huava.cloud.admin.mapper.AttachmentMapper;
import cn.huava.cloud.admin.pojo.po.AttachmentPo;
import cn.huava.cloud.admin.service.attachment.AceAttachmentService;
import cn.huava.cloud.common.controller.BaseController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 附件控制器
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/attachment")
public class AttachmentController
    extends BaseController<AceAttachmentService, AttachmentMapper, AttachmentPo> {

  @PostMapping("/auth/upload")
  public ResponseEntity<AttachmentPo> upload(@NonNull final MultipartHttpServletRequest req) {
    return ResponseEntity.ok(service.upload(req));
  }
}
