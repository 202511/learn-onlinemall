package com.own.onlinemall.product.exception;


import com.own.onlinemall.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//
//@Slf4j
//@RestControllerAdvice(basePackages = "com.own.onlinemall.product.controller")
//public class OnlinemallExceptionControllerAdvice {
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public Result handleVaildException(MethodArgumentNotValidException e)
//    {
//       BindingResult bindingResult= e.getBindingResult();
//        Map<String , String >  errorMap=new HashMap<>();
//        bindingResult.getFieldErrors().forEach((fieldError -> {
//            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }));
//         log.error("数据校验出现问题{}，异常类型：{}",e.getMessage(),e.getClass() );
//        System.out.println("哈哈哈哈哈");
//         Result t=new Result<Object>();
//         t.setData(errorMap);
//         return t.error(400,"数据校验出现问题");
//    }
//    @ExceptionHandler(value = Exception.class)
//    public Result handleVaildException(Exception e)
//    {
//
//        log.error("数据校验出现问题{}，异常类型：{}",e.getMessage(),e.getClass() );
//        Result t=new Result<Object>();
//        return t.error(400,"数据校验出现问题");
//    }
//
//}
