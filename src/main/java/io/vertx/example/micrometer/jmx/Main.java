
package io.vertx.example.micrometer.jmx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.micrometer.verticles.EventbusConsumer;
import io.vertx.example.micrometer.verticles.EventbusProducer;
import io.vertx.example.micrometer.verticles.SimpleWebServer;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxInfluxDbOptions;
import io.vertx.micrometer.VertxJmxMetricsOptions;

public final class Main {
  public static void main(String[] args) {
    // Default JMX options will publish MBeans under domain "metrics"
    MicrometerMetricsOptions options = new MicrometerMetricsOptions()
      .setJmxMetricsOptions(new VertxJmxMetricsOptions().setEnabled(true))
      .setEnabled(true);
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));
    vertx.deployVerticle(new SimpleWebServer());
    vertx.deployVerticle(new EventbusConsumer());
    vertx.deployVerticle(new EventbusProducer());
  }
}
