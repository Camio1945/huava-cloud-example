package cn.huava.cloud.member.validation;

import jakarta.validation.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户名唯一性校验器注解，与 {@link UniqueUsernameValidator} 配合使用
 *
 * @author Camio1945
 */
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
