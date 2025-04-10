package com.financeTracker.annotation;

import com.financeTracker.aspect.SecuredServletAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom security annotation used to mark servlet methods or classes
 * that require a specific user role for access.
 * This annotation is intended to be used with {@link SecuredServletAspect},
 * which checks the current user's role before proceeding with method execution.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Secured {

   /**
    * The required user role for accessing the annotated method or class.
    *
    * @return the name of the role that is allowed to access
    */
   String hasRole();
}