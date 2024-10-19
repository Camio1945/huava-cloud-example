package cn.huava.cloud.admin.validation.user;

import jakarta.validation.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户所拥有的角色 ids 校验器注解，与 {@link RoleIdsValidator} 配合使用
 *
 * @author Camio1945
 */
@Constraint(validatedBy = RoleIdsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleIds {
  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
