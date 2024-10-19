package cn.huava.cloud.admin.mapper;

import cn.huava.cloud.admin.pojo.po.RolePermPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色所拥有的权限 Mapper 持久化层
 *
 * @author Camio1945
 */
@Mapper
public interface RolePermMapper extends BaseMapper<RolePermPo> {}
