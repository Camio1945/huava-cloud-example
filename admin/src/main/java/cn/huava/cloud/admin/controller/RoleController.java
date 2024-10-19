package cn.huava.cloud.admin.controller;

import cn.huava.cloud.common.controller.BaseController;
import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.admin.mapper.RoleMapper;
import cn.huava.cloud.admin.pojo.po.RolePo;
import cn.huava.cloud.admin.pojo.qo.SetPermQo;
import cn.huava.cloud.admin.service.role.AceRoleService;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制器
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController extends BaseController<AceRoleService, RoleMapper, RolePo> {

  @GetMapping("/auth/isNameExists")
  public ResponseEntity<Boolean> isNameExists(final Long id, @NonNull final String name) {
    return ResponseEntity.ok(service.isNameExists(id, name));
  }

  @GetMapping("/auth/page")
  public ResponseEntity<PageDto<RolePo>> page(
      @NonNull final PageQo<RolePo> pageQo, @NonNull final RolePo params) {
    PageDto<RolePo> pageDto = service.rolePage(pageQo, params);
    return ResponseEntity.ok(pageDto);
  }

  @PostMapping("/auth/setPerm")
  public ResponseEntity<Void> setPerm(@RequestBody @NonNull @Validated final SetPermQo setPermQo) {
    service.setPerm(setPermQo);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/auth/getPerm/{id}")
  public ResponseEntity<List<Long>> getPerm(@PathVariable @NonNull final Long id) {
    return ResponseEntity.ok(service.getPerm(id));
  }

  /**
   * 根据角色 ID 获取权限 URI 列表
   *
   * @param roleId 角色 ID
   * @return 权限 URI 列表
   */
  @GetMapping("/auth/getPermUrisByRoleId/{roleId}")
  public ResponseEntity<Set<String>> getPermUrisByRoleId(@PathVariable @NonNull final Long roleId) {
    return ResponseEntity.ok(service.getPermUrisByRoleId(roleId));
  }
}
