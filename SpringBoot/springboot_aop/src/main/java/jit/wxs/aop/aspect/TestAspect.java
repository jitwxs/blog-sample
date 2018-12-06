package jit.wxs.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class TestAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* jit.wxs.aop.controller.*Controller.*(..))")
    public void pointCunt(){}

    @Before("pointCunt()")
    public void doBefore(JoinPoint joinPoint) {
        logger.info("---前置通知---");

        // 获取目标方法的参数信息
        logger.info("目标对象参数信息: {}", Arrays.toString(joinPoint.getArgs()));

        // AOP代理类的信息
        logger.info("AOP代理类: {}", joinPoint.getThis());

        // 代理的目标对象
        logger.info("代理的目标对象: {}", joinPoint.getTarget());

        // 通知的签名
        Signature signature = joinPoint.getSignature();
        // 代理的是哪一个方法
        logger.info("代理的方法名: {}", signature.getName());

        // AOP代理类的名字
        logger.info("AOP代理类的名字: {}", signature.getDeclaringTypeName());

        // AOP代理类的类信息
        logger.info("AOP代理类的类信息: {}", signature.getDeclaringType());

//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 获取 HttpServletRequest
//        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        // 获取 Session
//        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
    }

    @AfterReturning(returning = "ret", pointcut = "pointCunt()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        logger.info("---后置返回通知---");
        logger.info("代理方法: {}, 返回参数: {} ", joinPoint.getSignature().getName(), ret);
    }

    @AfterThrowing(pointcut = "pointCunt()", throwing = "e")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable e) {
        logger.info("---后置异常通知---");
        logger.info("代理方法: {}, 异常信息: {} ", joinPoint.getSignature().getName(), e.getMessage());
    }

    @After("pointCunt()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        logger.info("---后置最终通知---");
    }

    @Around("pointCunt()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        logger.info("---环绕通知开始---");
        logger.info("代理方法: {}", proceedingJoinPoint.getSignature().getName());
        try {
            Object obj = proceedingJoinPoint.proceed();
            logger.info("---环绕通知结束---");
            return obj;
        } catch (Throwable throwable) {
            logger.info("---环绕通知异常---");
            throwable.printStackTrace();
        }
        return null;
    }
}