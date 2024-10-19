package cn.huava.cloud.admin.service.role;

import cn.huava.cloud.admin.pojo.po.RolePo;
import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.mapper.RoleMapper;
import cn.huava.cloud.admin.pojo.po.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class RolePageService extends BaseService<RoleMapper, RolePo> {

  protected PageDto<RolePo> rolePage(@NonNull PageQo<RolePo> pageQo, @NonNull final RolePo params) {
    Wrapper<RolePo> wrapper =
        Fn.undeletedWrapper(RolePo::getDeleteInfo)
            .like(Fn.isNotBlank(params.getName()), RolePo::getName, params.getName())
            .like(
                Fn.isNotBlank(params.getDescription()),
                RolePo::getDescription,
                params.getDescription())
            .orderByAsc(RolePo::getSort);
    pageQo = page(pageQo, wrapper);
    return new PageDto<>(pageQo.getRecords(), pageQo.getTotal());
  }
}
