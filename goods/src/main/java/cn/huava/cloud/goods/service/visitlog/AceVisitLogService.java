package cn.huava.cloud.goods.service.visitlog;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.goods.mapper.VisitLogMapper;
import cn.huava.cloud.goods.pojo.po.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商品访问记录服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceVisitLogService extends BaseService<VisitLogMapper, VisitLogPo> {}
