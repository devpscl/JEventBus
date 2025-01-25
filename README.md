<img src="https://img.shields.io/static/v1?label=JDK&message=17%2B&color=3495eb&logoColor=white" alt="JDK" />

# JEventBus
Simple and lightweight event bus library written in java.

# Usage Gradle

```groovy
repositories {
  maven {
    url("http://repo.devpscl.de/repository/maven-public/")
    allowInsecureProtocol = true
  }
}

dependencies {
    implementation("net.devpscl:jeventbus:1.1.0")
}
```

# Example

### Initialize event bus
```java
EventBus eventBus = EventBus.builder()
            .enableListenerPriorities()
            .build();
eventBus.subscribe(MessageEvent.class, this::onMessage);
eventBus.register(this);


```

### Send event
```java
eventBus.post(new MessageEvent("Hello World"));
```

### Listen to events
```java
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
```
Some examples: [Click Here](https://github.com/devpscl/JEventBus/tree/master/src/test/java/net/devpscl/eventbus/example)
