package net.devpscl.eventbus;

import net.devpscl.eventbus.util.ErrorHandler;
import org.jetbrains.annotations.NotNull;

class BuilderImpl implements EventBus.Builder {

  private ErrorHandler errorHandler = null;
  private boolean enablePriorities = false;

  @Override
  public @NotNull EventBus.Builder errorHandler(@NotNull ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
    return this;
  }

  @Override
  public @NotNull EventBus.Builder enableListenerPriorities() {
    enablePriorities = true;
    return this;
  }

  @Override
  public @NotNull EventBus build() {
    return new EventBusImpl(enablePriorities, errorHandler);
  }
}
