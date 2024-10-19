package cn.huava.cloud.admin.service.attachment;

import cn.huava.cloud.admin.mapper.AttachmentMapper;
import cn.huava.cloud.admin.pojo.po.AttachmentPo;
import cn.huava.cloud.common.service.BaseService;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 基础上传服务 <br>
 * 注：因为调用的都是子类的方法，所以这个类在单元测试覆盖率中扫描不到，可以忽略。
 *
 * @author Camio1945
 */
public abstract class BaseUploadService extends BaseService<AttachmentMapper, AttachmentPo> {

  /**
   * upload file
   *
   * @param req request (must be multipart request)
   * @return attachmentPo
   */
  protected abstract AttachmentPo upload(@NonNull final MultipartHttpServletRequest req);
}
