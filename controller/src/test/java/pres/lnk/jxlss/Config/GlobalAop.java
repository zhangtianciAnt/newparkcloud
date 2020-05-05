package pres.lnk.jxlss.Config;

import cn.hutool.core.date.DateUtil;
import com.nt.controller.Start;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Configuration
public class GlobalAop {
    private static Logger log = LoggerFactory.getLogger(Start.class);
    @Pointcut("execution(public * com.nt.controller.Controller..*.*(..))")
    public void webLog(){}

    //调用接口前
    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("方法调用开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("接口地址 : " + request.getRequestURL().toString());
        log.info("请求时间 : "+ DateUtil.format(new Date(),"YYYY/MM/dd HH:mm:ss"));
        log.info("请求类型 : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("类名 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
        log.info("方法调用开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    //接口返回值
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {

//        encoder
//                ((ApiResult)ret).setData(Base64.encode(JSONUtil.parse(((ApiResult) ret).getData()).toStringPretty()));
        // 处理完请求，返回内容
//        if( ret !=null && ((ApiResult)ret).getData() != null){
//            log.info("返回值 : " + JSONUtil.parse(((ApiResult) ret).getData()).toStringPretty());
//        }else{
//            log.info("返回值 : 无");
//        }
    }

    //接口结束
    @After("webLog()")
    public void after(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("方法调用结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("接口地址 : " + request.getRequestURL().toString());
        log.info("TIME : "+ DateUtil.format(new Date(),"YYYY/MM/dd HH:mm:ss"));
        log.info("方法调用结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
