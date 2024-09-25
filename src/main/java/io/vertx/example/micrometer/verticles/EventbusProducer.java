package io.vertx.example.micrometer.verticles;

import io.vertx.core.AbstractVerticle;


public class EventbusProducer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(1000, x -> {
      Greetings.get(vertx)
        .onComplete(greetingResult -> vertx.eventBus().send("greeting", greetingResult.result()));
    });
  }
}
