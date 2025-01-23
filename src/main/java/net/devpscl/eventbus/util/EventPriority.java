package net.devpscl.eventbus.util;

import org.jetbrains.annotations.NotNull;

public enum EventPriority {
  LOWEST,
  LOW,
  DEFAULT,
  HIGH,
  HIGHEST;


  public static @NotNull EventPriority defaultValue() {
    return EventPriority.DEFAULT;
  }

}
