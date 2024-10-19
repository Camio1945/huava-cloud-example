package cn.huava.cloud.member.service.pointlog;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.mapper.PointLogMapper;
import cn.huava.cloud.member.pojo.po.PointLogPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 积分记录主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AcePointLogService extends BaseService<PointLogMapper, PointLogPo> {}
