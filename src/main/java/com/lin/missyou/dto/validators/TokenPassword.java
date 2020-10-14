package com.lin.missyou.dto.validators;

import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = TokenPasswordValidator.class)
public @interface TokenPassword {
    String message() default "信息不符合要求";
    int min() default 4;
    int max() default 8;

    // 注解的2个模板方法
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
