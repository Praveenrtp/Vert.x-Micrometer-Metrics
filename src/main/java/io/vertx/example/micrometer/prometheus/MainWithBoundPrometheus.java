
package io.vertx.example.micrometer.prometheus;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.micrometer.verticles.EventbusConsumer;
import io.vertx.example.micrometer.verticles.EventbusProducer;
import io.vertx.example.micrometer.verticles.WebServerForBoundPrometheus;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;


public final class MainWithBoundPrometheus {
  public static void main(String[] args) {
    // Deploy without embedded server: we need to "manually" expose the prometheus metrics
    MicrometerMetricsOptions options = new MicrometerMetricsOptions()
      .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
      .setEnabled(true);
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));
    vertx.deployVerticle(new WebServerForBoundPrometheus());
    vertx.deployVerticle(new EventbusConsumer());
    vertx.deployVerticle(new EventbusProducer());
  }
}
