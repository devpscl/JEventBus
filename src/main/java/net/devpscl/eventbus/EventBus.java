package net.devpscl.eventbus;

import net.devpscl.eventbus.listener.EventListener;
import net.devpscl.eventbus.listener.ListenerConfig;
import net.devpscl.eventbus.util.ErrorHandler;
import org.jetbrains.annotations.NotNull;
import net.devpscl.eventbus.annotation.SubscribeEvent;

import java.util.Collection;

/**
 * The interface Event bus.
 */
public interface EventBus {


  /**
   * Subscribe new listener for specific event type.
   * The default config is used {@code new ListenerConfig()}
   * @param <T>      the generic type of event
   * @param type     the event class type
   * @param listener the listener
   * @return self
   */
  <T extends Event> @NotNull EventBus subscribe(@NotNull Class<T> type,
                                                @NotNull EventListener<T> listener);

  /**
   * Subscribe new listener for specific event type with own configuration.
   * If listener priorities property enabled in builder, the listeners are sorted by lowest to highest priority.
   *
   * @param <T>      the generic type of event
   * @param type     the event class type
   * @param listener the listener
   * @param config   the configuration {@code new ListenerConfig()...}
   * @return self
   */
  <T extends Event> @NotNull EventBus subscribe(@NotNull Class<T> type,
                                                @NotNull EventListener<T> listener,
                                                @NotNull ListenerConfig config);

  /**
   * Register all subscribing methods from class object.
   * All public methods which annotated with {@link SubscribeEvent} are included.
   * If listener priorities property enabled in builder, the listeners are sorted by lowest to highest priority.
   * The priority can set with annotation constant {@link SubscribeEvent#priority()}.
   *
   * @param eventHandlerObject the event handler object
   * @return self
   */
  @NotNull EventBus register(@NotNull Object eventHandlerObject);

  /**
   * Register all subscribing methods from class object.
   * All public methods which annotated with {@link SubscribeEvent} and
   * the constant {@link SubscribeEvent#tag()} in annotation equaling to parameter {@code tag} are included.
   * The configuration owner is set to {@code eventHandlerObject}.
   * If listener priorities property enabled in builder, the listeners are sorted by lowest to highest priority.
   * The priority can set with annotation constant {@link SubscribeEvent#priority()}.
   * The tag filter is recommend for multiple event buses on the same class.
   *
   * @param eventHandlerObject the event handler object
   * @param tag                the tag
   * @return self
   */
  @NotNull EventBus register(@NotNull Object eventHandlerObject, @NotNull String tag);

  /**
   * Post event to all registered listeners.
   *
   * @param event the event
   * @return self
   */
  @NotNull EventBus post(@NotNull Event event);

  /**
   * Unregister listener.
   *
   * @param listener the listener
   * @return self
   */
  @NotNull EventBus unregister(@NotNull EventListener<?> listener);

  /**
   * Unregister all listeners for a specific event type.
   *
   * @param type the event class type
   * @return self
   */
  @NotNull EventBus unregister(@NotNull Class<? extends Event> type);

  /**
   * Unregister all listeners whose configuration owner is same to {@code owner} parameter.
   *
   * @param owner the owner object
   * @return self
   */
  @NotNull EventBus unregister(@NotNull Object owner);

  /**
   * Returns all events that are listened
   *
   * @return the collection of event class types
   */
  @NotNull Collection<Class<? extends Event>> listeningEventTypes();

  /**
   * Check if any listener is listen to event type.
   *
   * @param eventType the event class type
   * @return state
   * */
  boolean isListenedTo(@NotNull Class<? extends Event> eventType);

  /**
   * Create new builder
   *
   * @return the builder instance
   */
  static @NotNull Builder builder() {
    return new BuilderImpl();
  }

  /**
   * Create new event bus with default config.
   * @see Builder default configuration
   * @return the builder instance
   */
  static @NotNull EventBus createDefault() {
    return builder().build();
  }

  /**
   * The interface Builder.
   * At default, listener priorities are disabled and
   * {@link net.devpscl.eventbus.util.ConsoleErrorHandler} is set as error handler.
   */
  interface Builder {

    /**
     * Set error handler to handle errors while executing event listeners.
     *
     * @param errorHandler the error handler
     * @return self
     */
    @NotNull Builder errorHandler(@NotNull ErrorHandler errorHandler);

    /**
     * Enable to sort listeners by their priorities
     *
     * @return self
     */
    @NotNull Builder enableListenerPriorities();

    /**
     * Create event bus.
     *
     * @return the event bus
     */
    @NotNull EventBus build();

  }

}
