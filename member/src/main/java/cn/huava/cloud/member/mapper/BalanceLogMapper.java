package cn.huava.cloud.member.mapper;

import cn.huava.cloud.member.pojo.po.BalanceLogPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 余额记录 Mapper 持久化层
 *
 * @author Camio1945
 */
@Mapper
public interface BalanceLogMapper extends BaseMapper<BalanceLogPo> {}
