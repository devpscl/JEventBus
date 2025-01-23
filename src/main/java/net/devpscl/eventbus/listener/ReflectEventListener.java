package net.devpscl.eventbus.listener;

import net.devpscl.eventbus.Event;
import net.devpscl.eventbus.EventException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectEventListener<T extends Event> implements EventListener<T> {

  private final Object source;
  private final Method method;

  public ReflectEventListener(@NotNull Object source, @NotNull Method method) {
    this.source = source;
    this.method = method;
  }

  @Override
  public void onEvent(T event) throws EventException {
    try {
      method.invoke(source, event);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new EventException("Failed to call method " + method.getName(), e);
    }
  }

}
