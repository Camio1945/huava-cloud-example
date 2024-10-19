package cn.huava.cloud.admin.mapper;

import cn.huava.cloud.admin.pojo.po.UserExtPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 持久化层
 *
 * @author Camio1945
 */
@Mapper
public interface UserMapper extends BaseMapper<UserExtPo> {}
