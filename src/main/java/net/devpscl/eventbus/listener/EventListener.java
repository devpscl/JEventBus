package net.devpscl.eventbus.listener;

import net.devpscl.eventbus.Event;
import net.devpscl.eventbus.EventException;

public interface EventListener<T extends Event> {

  void onEvent(T event) throws EventException;

}
