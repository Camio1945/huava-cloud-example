package cn.huava.cloud.admin.service.role;

import cn.huava.cloud.admin.pojo.po.RolePermPo;
import cn.huava.cloud.admin.pojo.po.RolePo;
import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.cache.RoleCache;
import cn.huava.cloud.admin.mapper.RoleMapper;
import cn.huava.cloud.admin.pojo.po.*;
import cn.huava.cloud.admin.pojo.qo.SetPermQo;
import cn.huava.cloud.admin.service.roleperm.AceRolePermService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;
import java.util.Set;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceRoleService extends BaseService<RoleMapper, RolePo> {
  private final RolePageService rolePageService;
  private final AceRolePermService rolePermService;
  private final RoleCache roleCache;

  public PageDto<RolePo> rolePage(@NonNull PageQo<RolePo> pageQo, @NonNull final RolePo params) {
    return rolePageService.rolePage(pageQo, params);
  }

  public boolean isNameExists(Long id, @NonNull String name) {
    return exists(
        Fn.undeletedWrapper(RolePo::getDeleteInfo)
            .eq(RolePo::getName, name)
            .ne(id != null, RolePo::getId, id));
  }

  @Transactional(rollbackFor = Throwable.class)
  public void setPerm(@NonNull SetPermQo setPermQo) {
    Long roleId = setPermQo.getRoleId();
    rolePermService.remove(new LambdaQueryWrapper<RolePermPo>().eq(RolePermPo::getRoleId, roleId));
    List<Long> permIds = setPermQo.getPermIds();
    if (CollUtil.isNotEmpty(permIds)) {
      List<RolePermPo> rolePermPos =
          permIds.stream().map(permId -> new RolePermPo(roleId, permId)).toList();
      rolePermService.saveBatch(rolePermPos);
    }
    roleCache.deleteCache(roleId);
  }

  public List<Long> getPerm(@NonNull Long id) {
    return rolePermService
        .list(new LambdaQueryWrapper<RolePermPo>().eq(RolePermPo::getRoleId, id))
        .stream()
        .map(RolePermPo::getPermId)
        .toList();
  }

  public Set<String> getPermUrisByRoleId(@NonNull Long roleId) {
    return roleCache.getPermUrisByRoleId(roleId);
  }
}
