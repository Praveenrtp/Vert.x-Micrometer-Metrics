package io.vertx.example.micrometer.verticles;

import io.vertx.core.AbstractVerticle;


public class EventbusConsumer extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.eventBus().<String>consumer("greeting", message -> {
      String greeting = message.body();
      System.out.println("Received: " + greeting);
      Greetings.get(vertx)
        .onComplete(greetingResult -> message.reply(greetingResult.result()));
    });
  }
}
