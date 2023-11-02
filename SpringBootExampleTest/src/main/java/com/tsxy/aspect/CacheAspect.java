package com.tsxy.aspect;

import com.alibaba.fastjson.JSON;
import com.tsxy.annotation.CacheLock;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Liu_df
 * @Date 2023/11/2 9:44
 */

@Aspect
@Component
public class CacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);

    private final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<>();

    private final ExpressionParser parser = new SpelExpressionParser();


    @Around("@annotation(com.tsxy.annotation.CacheLock)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("CacheAspect.Around开始");

        Object[] args = joinPoint.getArgs();    // 接口给的参数

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        CacheLock cacheLock = method.getAnnotation(CacheLock.class);

        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);

        logger.info("parameterNames--->{}", JSON.toJSONString(parameterNames));
        logger.info("args--->{}", JSON.toJSONString(args));

        for (String s : cacheLock.key()) {
            String key = getKey(s, parameterNames, args);
            logger.info("key--->{}", key);
        }
        Object proceed = joinPoint.proceed();
        logger.info("返回结果--->{}", JSON.toJSONString(proceed));
        return proceed;
    }

    public String getKey(String key, String[] parameterNames, Object[] args) {
        try {
            if (!StringUtils.contains(key, "#")) {
                return key;
            }
            EvaluationContext ctx = new StandardEvaluationContext();
            int i = 0;
            for (String param : parameterNames) {
                ctx.setVariable(param, args[i]);
                i++;
            }

            Expression expression = expCache.get(key);
            if (null == expression) {
                expression = parser.parseExpression(key);
                expCache.put(key, expression);
            }
            return String.valueOf(expression.getValue(ctx));
        } catch (Exception e) {
            logger.error("error:", e);
            return "";
        }
    }

}
