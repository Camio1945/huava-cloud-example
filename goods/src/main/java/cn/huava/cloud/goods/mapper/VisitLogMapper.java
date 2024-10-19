package cn.huava.cloud.goods.mapper;

import cn.huava.cloud.goods.pojo.po.VisitLogPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
 * 商品访问记录 Mapper 持久化层
 *
 * @author Camio1945
 */
@Mapper
public interface VisitLogMapper extends BaseMapper<VisitLogPo> {}
