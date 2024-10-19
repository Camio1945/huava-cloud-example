package cn.huava.cloud.admin.service.perm;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.mapper.PermMapper;
import cn.huava.cloud.admin.pojo.dto.PermDto;
import cn.huava.cloud.admin.pojo.po.PermPo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 权限服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AcePermService extends BaseService<PermMapper, PermPo> {
  private final GetAllPermService getAllPermService;

  public List<PermDto> getAllPerm(boolean excludeElement) {
    return getAllPermService.getAllPerm(excludeElement);
  }
}
