package net.devpscl.eventbus.util;

import net.devpscl.eventbus.Event;
import org.jetbrains.annotations.NotNull;

public interface ErrorHandler {

  void handleError(@NotNull Throwable throwable, @NotNull Event event);

}
