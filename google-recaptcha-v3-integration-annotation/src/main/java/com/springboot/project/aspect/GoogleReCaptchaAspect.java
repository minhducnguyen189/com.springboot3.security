package com.springboot.project.aspect;

import com.springboot.project.exception.UnAuthorizationException;
import com.springboot.project.service.RecaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class GoogleReCaptchaAspect {

    private static final String CAPTCHA_HEADER_NAME = "google-recaptcha-response";

    private final RecaptchaService recaptchaService;

    @Autowired
    public GoogleReCaptchaAspect(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Around("@annotation(com.springboot.project.annotation.GoogleReCaptchaProtect)")
    public Object validateGoogleReCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String captchaResponse = request.getHeader(CAPTCHA_HEADER_NAME);
        boolean isValidCaptcha = recaptchaService.verifyRecaptcha(captchaResponse);
        if(!isValidCaptcha){
            throw new UnAuthorizationException("Invalid recaptcha");
        }
        return joinPoint.proceed();
    }

}
