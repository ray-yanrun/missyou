package com.lin.missyou.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordEquals {
    // 定义密码最小长度
    int min() default 4;

    // 定义密码最大长度
    int max() default 6;

    // 定义提示信息
    String message() default "passwords are not equal";

    // 注解的2个模板方法
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
