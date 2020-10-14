package com.lin.missyou.dto.validators;

import com.lin.missyou.dto.PersonDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 第一个参数：关联的注解
 * 第二个参数：修饰的目标的类型
 */
public class PasswordValidator implements ConstraintValidator<PasswordEquals, PersonDTO> {

    private int min;
    private int max;

    @Override
    public void initialize(PasswordEquals constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(PersonDTO personDTO, ConstraintValidatorContext constraintValidatorContext) {
        String password1 = personDTO.getPassword1();
        String password2 = personDTO.getPassword2();
        boolean minValid = password1.length()>=min && password2.length()>=min;
        boolean maxValid = password1.length()<=max && password2.length()<=max;
        return password1.equals(password2) && minValid && maxValid;
    }
}
