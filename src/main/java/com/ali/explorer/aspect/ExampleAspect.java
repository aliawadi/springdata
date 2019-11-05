package com.ali.explorer.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExampleAspect {


    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws  Throwable{
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        System.out.println(joinPoint.getSignature() + " executed in " + (System.currentTimeMillis() - start) + " ms");
        return proceed;
    }


}

