package tz.go.roadsfund.nrcc.security;

import tz.go.roadsfund.nrcc.enums.Permission;

import java.lang.annotation.*;

/**
 * Annotation for method-level permission checking
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    /**
     * Required permissions (user must have ALL)
     */
    Permission[] value() default {};

    /**
     * Alternative permissions (user must have ANY)
     */
    Permission[] anyOf() default {};
}
