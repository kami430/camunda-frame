package com.camunda.demo.base.constant;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IConstValidator.class)
public @interface IConst {
    Class value() default Object.class;

    String message() default "不符合数据限制";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
