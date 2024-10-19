package cn.huava.cloud.member.service.balancelog;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.mapper.BalanceLogMapper;
import cn.huava.cloud.member.pojo.po.BalanceLogPo;
import cn.huava.cloud.member.service.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 余额记录主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceBalanceLogService extends BaseService<BalanceLogMapper, BalanceLogPo> {}
