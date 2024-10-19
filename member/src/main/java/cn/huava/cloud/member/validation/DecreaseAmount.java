package cn.huava.cloud.member.validation;

import jakarta.validation.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 减少用户余额校验器注解，与 {@link DecreaseAmountValidator} 配合使用
 *
 * @author Camio1945
 */
@Constraint(validatedBy = DecreaseAmountValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecreaseAmount {
  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
