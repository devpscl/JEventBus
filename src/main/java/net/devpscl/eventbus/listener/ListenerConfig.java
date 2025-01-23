package net.devpscl.eventbus.listener;

import net.devpscl.eventbus.util.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListenerConfig {

  private EventPriority priority;
  private Object ownerObject;

  public ListenerConfig() {
    this(EventPriority.defaultValue(), null);
  }

  public ListenerConfig(@NotNull EventPriority priority, @Nullable Object ownerObject) {
    this.priority = priority;
    this.ownerObject = ownerObject;
  }

  public @NotNull EventPriority priority() {
    return priority;
  }

  public @Nullable Object ownerObject() {
    return ownerObject;
  }

  public @NotNull ListenerConfig priority(@NotNull EventPriority priority) {
    this.priority = priority;
    return this;
  }

  public @NotNull ListenerConfig ownerObject(@Nullable Object ownerObject) {
    this.ownerObject = ownerObject;
    return this;
  }

  public @NotNull ListenerConfig copy() {
    return new ListenerConfig(priority, ownerObject);
  }

}
