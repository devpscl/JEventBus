package net.devpscl.eventbus.example;

import net.devpscl.eventbus.EventBus;
import net.devpscl.eventbus.annotation.SubscribeEvent;
import net.devpscl.eventbus.util.EventPriority;

public class Example {

  private EventBus eventBus;

  public static void main(String[] args) {

    Example example = new Example();
    example.init();
    example.doSomething();

    MultipleBusExample multipleBusExample = new MultipleBusExample();
    multipleBusExample.init();
    multipleBusExample.doSomething();
  }

  ///////////////////////////////////////////////////////////////////////////
  // Register new event bus
  ///////////////////////////////////////////////////////////////////////////

  public void init() {
    eventBus = EventBus.builder()
            .enableListenerPriorities()
            .build();
    eventBus.subscribe(MessageEvent.class, this::onMessage);
    eventBus.register(this);
  }

  public void doSomething() {
    eventBus.post(new MessageEvent("Hello World"));
    InputEvent inputEvent = new InputEvent('p');
    eventBus.post(inputEvent);
    if(!inputEvent.cancelAction()) {
      System.out.println("Input not cancelled");
      eventBus.unregister(this); // remove onInputBefore, onInputBefore
    }
   }

  ///////////////////////////////////////////////////////////////////////////
  // Some event listener options
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Manually subscribed to eventbus
   * */
  public void onMessage(MessageEvent event) {
    System.out.println("Message: " + event.message());
  }

  /**
   * Automatically subscribed to eventbus
   * */
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onInputAfter(InputEvent e) {
    e.setCancelAction(true);
  }

  /**
   * Automatically subscribed to eventbus
   * */
  @SubscribeEvent
  public void onInputBefore(InputEvent e) {
    System.out.println("Input: " + e.input());
    e.setCancelAction(false); //
  }

}
