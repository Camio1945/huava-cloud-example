package cn.huava.cloud.admin.service.user;

import static java.util.stream.Collectors.toSet;

import cn.huava.cloud.admin.pojo.po.*;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.common.constant.CommonConstant;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.cache.UserRoleCache;
import cn.huava.cloud.admin.enumeration.PermTypeEnum;
import cn.huava.cloud.admin.mapper.UserMapper;
import cn.huava.cloud.admin.pojo.dto.PermDto;
import cn.huava.cloud.admin.pojo.dto.UserInfoDto;
import cn.huava.cloud.admin.pojo.po.*;
import cn.huava.cloud.admin.service.perm.AcePermService;
import cn.huava.cloud.admin.service.roleperm.AceRolePermService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 获取用户信息
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class GetUserInfoService extends BaseService<UserMapper, UserExtPo> {
  private final AceRolePermService rolePermService;
  private final AcePermService permService;
  private final UserRoleCache userRoleCache;

  protected UserInfoDto getUserInfoDto() {
    UserInfoDto userInfoDto = new UserInfoDto();
    UserPo loginUser = Fn.getLoginUser();
    setBaseInfo(loginUser, userInfoDto);
    setMenuAndPerms(loginUser, userInfoDto);
    return userInfoDto;
  }

  private static List<String> buildUris(List<PermPo> perms, boolean isAdmin, Set<Long> permIds) {
    if (isAdmin) {
      return List.of("*");
    }
    return perms.stream()
        .filter(perm -> Fn.isNotBlank(perm.getUri()))
        .filter(perm -> permIds.contains(perm.getId()))
        .map(PermPo::getUri)
        .collect(toSet())
        .stream()
        .sorted()
        .toList();
  }

  private void setBaseInfo(UserPo loginUser, UserInfoDto dto) {
    dto.setUsername(loginUser.getUsername());
    String avatar = loginUser.getAvatar();
    if (Fn.isNotBlank(avatar)) {
      HttpServletRequest req = Fn.getRequest();
      avatar =
          Fn.format(
              "{}://{}:{}{}", req.getScheme(), req.getServerName(), req.getServerPort(), avatar);
      dto.setAvatar(avatar);
    }
  }

  private void setMenuAndPerms(UserPo loginUser, UserInfoDto userInfoDto) {
    List<PermPo> perms = getPerms();
    // 根据用户 ID 或用户所属的角色 ID 来判断是否属于超级管理员
    boolean isAdmin = loginUser.getId() == CommonConstant.ADMIN_USER_ID;
    List<Long> roleIds = null;
    if (!isAdmin) {
      roleIds = userRoleCache.getRoleIdsByUserId(loginUser.getId());
      isAdmin = roleIds.contains(CommonConstant.ADMIN_ROLE_ID);
    }
    final Set<Long> permIds = getPermIds(isAdmin, roleIds);
    userInfoDto.setMenu(buildMenuTree(perms, isAdmin, permIds));
    userInfoDto.setUris(buildUris(perms, isAdmin, permIds));
  }

  /**
   * build the perms to tree, they are the menus that user can see on the left panel in their
   * browser
   */
  private List<PermDto> buildMenuTree(List<PermPo> perms, boolean isAdmin, Set<Long> permIds) {
    List<PermDto> menu =
        perms.stream()
            .filter(perm -> perm.getPid() == 0)
            .filter(perm -> !perm.getType().equals(PermTypeEnum.E.name()))
            .filter(perm -> isAdmin || permIds.contains(perm.getId()))
            .map(perm -> Fn.toBean(perm, PermDto.class))
            .toList();
    for (PermDto permDto : menu) {
      permDto.setChildren(getChildren(permDto.getId(), perms, isAdmin, permIds));
    }
    return menu;
  }

  /** Get all the perm ids the user have */
  private Set<Long> getPermIds(boolean isAdmin, List<Long> roleIds) {
    final Set<Long> permIds = new HashSet<>();
    if (!isAdmin) {
      Wrapper<RolePermPo> wrapper =
          new LambdaQueryWrapper<RolePermPo>()
              .in(RolePermPo::getRoleId, roleIds)
              .select(RolePermPo::getPermId);
      Set<Long> permIdSet =
          rolePermService.list(wrapper).stream().map(RolePermPo::getPermId).collect(toSet());
      permIds.addAll(permIdSet);
    }
    return permIds;
  }

  /** Get enabled perms, (just directories and menus, without elements) */
  private List<PermPo> getPerms() {
    LambdaQueryWrapper<PermPo> wrapper =
        Fn.undeletedWrapper(PermPo::getDeleteInfo)
            .eq(PermPo::getIsEnabled, true)
            .orderByAsc(PermPo::getSort);
    return permService.list(wrapper);
  }

  private List<PermDto> getChildren(
      long pid, List<PermPo> perms, boolean isAdmin, Set<Long> permIds) {
    List<PermDto> children =
        perms.stream()
            .filter(perm -> perm.getPid() == pid)
            .filter(perm -> !perm.getType().equals(PermTypeEnum.E.name()))
            .filter(perm -> isAdmin || permIds.contains(perm.getId()))
            .map(perm -> Fn.toBean(perm, PermDto.class))
            .toList();
    for (PermDto child : children) {
      child.setChildren(getChildren(child.getId(), perms, isAdmin, permIds));
    }
    return children;
  }
}
