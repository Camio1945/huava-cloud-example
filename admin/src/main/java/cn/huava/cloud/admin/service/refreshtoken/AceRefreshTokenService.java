package cn.huava.cloud.admin.service.refreshtoken;

import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.mapper.RefreshTokenMapper;
import cn.huava.cloud.admin.pojo.po.RefreshTokenPo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 刷新 token 服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@AllArgsConstructor
public class AceRefreshTokenService extends BaseService<RefreshTokenMapper, RefreshTokenPo> {
  public void saveRefreshToken(@NonNull Long sysUserId, @NonNull String refreshToken) {
    RefreshTokenPo po = new RefreshTokenPo().setRefreshToken(refreshToken).setSysUserId(sysUserId);
    BasePo.beforeCreate(po);
    save(po);
  }

  public RefreshTokenPo getByRefreshToken(@NonNull String refreshToken) {
    Wrapper<RefreshTokenPo> wrapper =
        Fn.undeletedWrapper(RefreshTokenPo::getDeleteInfo)
            .eq(RefreshTokenPo::getRefreshToken, refreshToken);
    return getOne(wrapper);
  }
}
