package net.devpscl.eventbus.example;

import net.devpscl.eventbus.Event;

public class InputEvent implements Event {

  private final char input;
  private boolean cancelAction = false;

  public InputEvent(char input) {
    this.input = input;
  }

  public void setCancelAction(boolean cancelAction) {
    this.cancelAction = cancelAction;
  }

  public boolean cancelAction() {
    return cancelAction;
  }

  public char input() {
    return input;
  }
}
