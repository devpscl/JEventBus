package net.devpscl.eventbus;

import net.devpscl.eventbus.annotation.SubscribeEvent;
import net.devpscl.eventbus.listener.EventListener;
import net.devpscl.eventbus.listener.ListenerConfig;
import net.devpscl.eventbus.listener.ReflectEventListener;
import net.devpscl.eventbus.util.ConsoleErrorHandler;
import net.devpscl.eventbus.util.ErrorHandler;
import net.devpscl.eventbus.util.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class EventBusImpl implements EventBus {

  private final boolean sortByPriority;
  private final ErrorHandler errorHandler;
  private final Map<Class<? extends Event>, List<Entry<?>>> map = new HashMap<>();
  private final ReentrantLock syncLock = new ReentrantLock();

  public EventBusImpl(boolean sortByPriority, @Nullable ErrorHandler errorHandler) {
    this.sortByPriority = sortByPriority;
    this.errorHandler = errorHandler == null ? new ConsoleErrorHandler() : errorHandler;
  }

  @SuppressWarnings("unchecked")
  private <T extends Event> void call(EventListener<T> listener, Event event) {
    try {
      listener.onEvent((T) event);
    } catch (Throwable throwable) {
      errorHandler.handleError(throwable, event);
    }
  }

  @Override
  public @NotNull <T extends Event> EventBus subscribe(@NotNull Class<T> type,
                                                       @NotNull EventListener<T> listener) {
    return subscribe(type, listener, new ListenerConfig());
  }

  @Override
  public @NotNull <T extends Event> EventBus subscribe(@NotNull Class<T> type,
                                                       @NotNull EventListener<T> listener,
                                                       @NotNull ListenerConfig config) {
    syncLock.lock();
    try {
      List<Entry<?>> entryList = map.getOrDefault(type, new ArrayList<>());
      entryList.add(new Entry<>(type, listener, config));
      if(sortByPriority) {
        Collections.sort(entryList);
      }
      map.put(type, entryList);
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus register(@NotNull Object eventHandlerObject) {
    return register(eventHandlerObject, "");
  }

  @Override
  public @NotNull EventBus register(@NotNull Object eventHandlerObject, @NotNull String tag) {
    Class<?> clazz = eventHandlerObject.getClass();
    for (Method method : clazz.getMethods()) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      if(parameterTypes.length != 1) {
        continue;
      }
      if(!method.isAnnotationPresent(SubscribeEvent.class)) {
        continue;
      }
      SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
      if(!Event.class.isAssignableFrom(parameterTypes[0])) {
        continue;
      }
      if(!tag.isEmpty()) {
        String[] tags = annotation.tag();
        boolean found = false;
        for (String str : tags) {
          if(str.equals(tag)) {
            found = true;
            break;
          }
        }
        if(!found) {
          continue;
        }
      }
      ListenerConfig config = new ListenerConfig(annotation.priority(), eventHandlerObject);
      Class<? extends Event> eventType = parameterTypes[0].asSubclass(Event.class);
      subscribe(eventType, new ReflectEventListener<>(eventHandlerObject, method), config);
    }
    return this;
  }

  @Override
  public @NotNull EventBus post(@NotNull Event event) {
    syncLock.lock();
    try {
      dispatchDirect(event);
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus unregister(@NotNull EventListener<?> listener) {
    syncLock.lock();
    try {
      for (Class<? extends Event> eventType : map.keySet()) {
        List<Entry<?>> entryList = map.get(eventType);
        entryList.removeIf(entry ->
                entry.listener() == listener
        );
      }
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus unregister(@NotNull Class<? extends Event> type) {
    syncLock.lock();
    try {
      map.remove(type);
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus unregister(@NotNull Object owner) {
    syncLock.lock();
    try {
      for (Class<? extends Event> eventType : map.keySet()) {
        List<Entry<?>> entryList = map.get(eventType);
        entryList.removeIf(entry ->
                entry.owner() == owner
        );
      }
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull Collection<Class<? extends Event>> listeningEventTypes() {
    syncLock.lock();
    try {
      ArrayList<Class<? extends Event>> copiedArrayList = new ArrayList<>(map.keySet());
      return Collections.unmodifiableCollection(copiedArrayList);
    } finally {
      syncLock.unlock();
    }
  }

  @Override
  public boolean isListenedTo(@NotNull Class<? extends Event> eventType) {
    if(!map.containsKey(eventType)) {
      return false;
    }
    return !map.get(eventType).isEmpty();
  }

  private void dispatchDirect(@NotNull Event event) {
    List<Entry<?>> entryList = map.get(event.getClass());
    if(entryList == null) {
      return;
    }
    for (Entry<?> entry : entryList) {
      call(entry.listener(), event);
    }
  }

  static class Entry<T extends Event> implements Comparable<Entry<?>> {

    private final Class<T> type;
    private final EventListener<T> listener;
    private final ListenerConfig config;

    public Entry(@NotNull Class<T> type,
                 @NotNull EventListener<T> listener,
                 @NotNull ListenerConfig config) {
      this.type = type;
      this.listener = listener;
      this.config = config;
    }

    public @NotNull Class<T> type() {
      return type;
    }

    public @NotNull EventListener<T> listener() {
      return listener;
    }

    public @NotNull EventPriority priority() {
      return config.priority();
    }

    public @Nullable Object owner() {
      return config.ownerObject();
    }

    @Override
    public int compareTo(@NotNull EventBusImpl.Entry<?> another) {
      return Integer.compare(priority().ordinal(), another.priority().ordinal());
    }

  }

}
