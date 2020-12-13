package com.camunda.demo.base.constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IConstValidator implements ConstraintValidator<IConst, Integer> {

    private Class constClass = null;

    @Override
    public void initialize(IConst constraintAnnotation) {
        constClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return IConstUtils.valudate(constClass, value);
    }
}
