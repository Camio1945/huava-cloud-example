package cn.huava.cloud.goods.mapper;

import cn.huava.cloud.goods.pojo.po.GoodsPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.NonNull;
import org.apache.ibatis.annotations.*;

/**
 * 商品 Mapper 持久化层
 *
 * @author Camio1945
 */
@Mapper
public interface GoodsMapper extends BaseMapper<GoodsPo> {

}
