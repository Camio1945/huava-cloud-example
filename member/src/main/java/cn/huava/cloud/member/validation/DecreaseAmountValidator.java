package cn.huava.cloud.member.validation;

import cn.huava.cloud.common.validation.BaseValidator;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.util.Fn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * 减少用户余额校验器，与 {@link DecreaseAmount} 注解配合使用
 *
 * @author Camio1945
 */
public class DecreaseAmountValidator extends BaseValidator
    implements ConstraintValidator<DecreaseAmount, BigDecimal> {

  @Override
  public boolean isValid(BigDecimal decreaseAmount, ConstraintValidatorContext context) {
    MemberPo member = Fn.getLoginMember();
    if (member.getBalance().compareTo(decreaseAmount) < 0) {
      return customMessage(context, "余额不足");
    }
    return true;
  }
}
