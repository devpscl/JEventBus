package net.devpscl.eventbus.annotation;

import net.devpscl.eventbus.util.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent {

  @NotNull EventPriority priority() default EventPriority.DEFAULT;
  @NotNull String[] tag() default "";

}
