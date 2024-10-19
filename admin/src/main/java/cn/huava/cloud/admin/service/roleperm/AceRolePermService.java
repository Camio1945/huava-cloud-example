package cn.huava.cloud.admin.service.roleperm;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.mapper.RolePermMapper;
import cn.huava.cloud.admin.pojo.po.RolePermPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色所拥有的权限服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceRolePermService extends BaseService<RolePermMapper, RolePermPo> {}
