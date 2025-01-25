package net.devpscl.eventbus.example;

import net.devpscl.eventbus.EventBus;
import net.devpscl.eventbus.annotation.SubscribeEvent;
import net.devpscl.eventbus.util.EventPriority;

public class MultipleBusExample {

  private final EventBus busA;
  private final EventBus busB;

  public MultipleBusExample() {
    this.busA = EventBus.builder().enableListenerPriorities().build();
    this.busB = EventBus.builder().enableListenerPriorities().build();
  }

  public void init() {
    busA.register(this, "bus_a");
    busB.register(this, "bus_b");
  }


  @SubscribeEvent(tag = {"bus_a", "bus_b"}, priority = EventPriority.LOWEST)
  public void onGlobalMessage(MessageEvent event) {
    System.out.println("Bus ?: " + event.message());
  }

  @SubscribeEvent(tag = {"bus_a"})
  public void onBusAMessage(MessageEvent event) {
    System.out.println("Bus A: message");
  }

  @SubscribeEvent(tag = {"bus_b"})
  public void onBusBMessage(MessageEvent event) {
    System.out.println("Bus B: message");
  }


  public void doSomething() {
    MessageEvent event = new MessageEvent("");
    System.out.println("Bus A listener results: ");
    busA.post(event);
    System.out.println("Bus B listener results: ");
    busB.post(event);
  }
}
