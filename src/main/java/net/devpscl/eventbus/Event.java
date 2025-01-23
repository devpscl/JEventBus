package net.devpscl.eventbus;

import org.jetbrains.annotations.NotNull;

public interface Event {

  default @NotNull String eventName() {
    return getClass().getSimpleName();
  }

}
