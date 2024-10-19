package cn.huava.cloud.admin.controller;

import cn.huava.cloud.admin.cache.UserCache;
import cn.huava.cloud.admin.cache.UserRoleCache;
import cn.huava.cloud.admin.mapper.UserMapper;
import cn.huava.cloud.admin.pojo.dto.*;
import cn.huava.cloud.admin.pojo.po.UserExtPo;
import cn.huava.cloud.admin.pojo.qo.LoginQo;
import cn.huava.cloud.admin.pojo.qo.UpdatePasswordQo;
import cn.huava.cloud.admin.service.user.AceUserService;
import cn.huava.cloud.admin.service.userrole.AceUserRoleService;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.common.controller.BaseController;
import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器（用于管理后台）<br>
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController<AceUserService, UserMapper, UserExtPo> {
  private final AceUserRoleService userRoleService;
  private final UserCache userCache;
  private final UserRoleCache userRoleCache;

  @GetMapping("/auth/page")
  public ResponseEntity<PageDto<UserDto>> page(
      @NonNull final PageQo<UserExtPo> pageQo, @NonNull final UserExtPo params) {
    PageDto<UserDto> pageDto = service.userPage(pageQo, params);
    return ResponseEntity.ok(pageDto);
  }

  @GetMapping("/auth/mySelf")
  public ResponseEntity<UserInfoDto> mySelf() {
    UserInfoDto userInfoDto = service.getUserInfoDto();
    return ResponseEntity.ok(userInfoDto);
  }

  @PostMapping("/auth/logout")
  public ResponseEntity<Void> logout(@RequestBody @NonNull String refreshToken) {
    service.logout(refreshToken);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/auth/isUsernameExists")
  public ResponseEntity<Boolean> isUsernameExists(final Long id, @NonNull final String username) {
    return ResponseEntity.ok(service.isUsernameExists(id, username));
  }

  /**
   * 获取用户所属的角色 ID 列表
   *
   * @param userId 用户 ID
   * @return 角色 ID 列表
   */
  @GetMapping("/auth/getRoleIdsByUserId/{userId}")
  public ResponseEntity<List<Long>> getRoleIdsByUserId(@PathVariable @NonNull final Long userId) {
    return ResponseEntity.ok(service.getRoleIdsByUserId(userId));
  }

  @PatchMapping("/auth/updatePassword")
  public ResponseEntity<Void> updatePassword(
      @RequestBody @NonNull @Validated UpdatePasswordQo updatePasswordQo) {
    service.updatePassword(updatePasswordQo);
    return ResponseEntity.ok(null);
  }

  @PostMapping("/free/login")
  public ResponseEntity<UserJwtDto> login(
      @NonNull final HttpServletRequest req, @RequestBody @NonNull final LoginQo loginQo) {
    return ResponseEntity.ok(service.login(req, loginQo));
  }

  @PostMapping("/free/refreshToken")
  public ResponseEntity<String> refreshToken(@RequestBody @NonNull String refreshToken) {
    String accessToken = service.refreshToken(refreshToken);
    return ResponseEntity.ok(accessToken);
  }
  @Override
  protected void afterGetById(@NonNull UserExtPo entity) {
    entity.setPassword(null);
    entity.setRoleIds(userRoleService.getRoleIdsByUserId(entity.getId()));
  }

  @Override
  protected void beforeSave(@NonNull UserExtPo entity) {
    entity.setPassword(Fn.encryptPassword(entity.getPassword()));
  }

  @Override
  protected void afterSave(@NonNull UserExtPo entity) {
    afterSaveOrUpdate(entity);
  }

  /**
   * If password is empty, then use the password stored in the database.<br>
   * If password is not empty, then encrypt it.<br>
   *
   * @param entity The entity to be updated.
   */
  @Override
  protected void beforeUpdate(@NonNull UserExtPo entity) {
    String password = entity.getPassword();
    UserExtPo before = userCache.getById(entity.getId());
    if (Fn.isBlank(password)) {
      String dbPassword = before.getPassword();
      entity.setPassword(dbPassword);
    } else {
      entity.setPassword(Fn.encryptPassword(password));
    }
    userCache.beforeUpdate(before);
  }

  @Override
  protected void afterUpdate(@NonNull UserExtPo entity) {
    afterSaveOrUpdate(entity);
  }

  @Override
  protected Object beforeDelete(@NonNull Long id) {
    return userCache.getById(id);
  }

  @Override
  protected void afterDelete(Object obj) {
    userCache.afterDelete((UserExtPo) obj);
  }

  private void afterSaveOrUpdate(@NonNull UserExtPo entity) {
    userRoleService.saveUserRole(entity.getId(), entity.getRoleIds());
    userCache.afterSaveOrUpdate(entity);
    userRoleCache.deleteCache(entity.getId());
  }
}
