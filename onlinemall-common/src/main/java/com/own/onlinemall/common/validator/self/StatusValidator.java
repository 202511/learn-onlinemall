package com.own.onlinemall.common.validator.self;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

public class StatusValidator implements ConstraintValidator<Status, Integer> {
     private  HashSet<Integer> hashSet=new HashSet<Integer>();
    //通过这个获取注解的信息
    @Override
    public void initialize(Status constraintAnnotation) {
        int[] value = constraintAnnotation.value();
        for (int i : value) {
            hashSet.add(i);
        }
    }

    //第一个参数就是前端传进来的数字
    //这个方法就是用来检验
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("进行校验");
        System.out.println(integer);
        if(hashSet.contains(integer))return true;
        return false;
    }
}
