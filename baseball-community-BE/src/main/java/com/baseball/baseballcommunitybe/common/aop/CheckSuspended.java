package com.baseball.baseballcommunitybe.common.aop;

import java.lang.annotation.*;

/**
 * 사용자의 정지(SUSPENDED) 상태를 확인하는 AOP용 어노테이션
 * Controller 메서드에 붙이면 AOP가 실행되어 접근을 차단함
 */
@Target(ElementType.METHOD)        // 메서드에만 붙일 수 있게
@Retention(RetentionPolicy.RUNTIME) // 런타임 시점에도 유지
@Documented
public @interface CheckSuspended {
}
