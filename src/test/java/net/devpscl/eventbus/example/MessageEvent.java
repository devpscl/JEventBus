package net.devpscl.eventbus.example;

import net.devpscl.eventbus.Event;

public class MessageEvent implements Event {

  private final String message;

  public MessageEvent(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}
