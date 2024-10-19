package cn.huava.cloud.member.mapper;

import cn.huava.cloud.member.pojo.po.MemberPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员 Mapper 持久化层
 *
 * @author Camio1945
 */
@Mapper
public interface MemberMapper extends BaseMapper<MemberPo> {}
