package net.devpscl.eventbus.util;

import net.devpscl.eventbus.Event;
import org.jetbrains.annotations.NotNull;

public class ConsoleErrorHandler implements ErrorHandler {

  @Override
  public void handleError(@NotNull Throwable throwable, @NotNull Event event) {
    System.err.println("Error at event " + event.eventName());
    throwable.printStackTrace(System.err);
  }

}
