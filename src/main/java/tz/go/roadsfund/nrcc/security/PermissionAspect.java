package tz.go.roadsfund.nrcc.security;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.exception.UnauthorizedException;

import java.lang.reflect.Method;

/**
 * Aspect for enforcing permission-based access control
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final PermissionChecker permissionChecker;

    @Around("@annotation(tz.go.roadsfund.nrcc.security.RequirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission requirePermission = method.getAnnotation(RequirePermission.class);

        // Check if user has all required permissions
        Permission[] requiredPermissions = requirePermission.value();
        if (requiredPermissions.length > 0) {
            if (!permissionChecker.hasAllPermissions(requiredPermissions)) {
                throw new UnauthorizedException("Insufficient permissions to perform this action");
            }
        }

        // Check if user has any of the alternative permissions
        Permission[] anyOfPermissions = requirePermission.anyOf();
        if (anyOfPermissions.length > 0) {
            if (!permissionChecker.hasAnyPermission(anyOfPermissions)) {
                throw new UnauthorizedException("Insufficient permissions to perform this action");
            }
        }

        return joinPoint.proceed();
    }
}
