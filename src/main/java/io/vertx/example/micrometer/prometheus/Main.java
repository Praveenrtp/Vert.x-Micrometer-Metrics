
package io.vertx.example.micrometer.prometheus;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.example.micrometer.verticles.EventbusConsumer;
import io.vertx.example.micrometer.verticles.EventbusProducer;
import io.vertx.example.micrometer.verticles.SimpleWebServer;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;


public final class Main {
  public static void main(String[] args) {
    // Deploy with embedded server: prometheus metrics will be automatically exposed,
    // independently from any other HTTP server defined
    MicrometerMetricsOptions options = new MicrometerMetricsOptions()
      .setPrometheusOptions(new VertxPrometheusOptions()
        .setStartEmbeddedServer(true)
        .setEmbeddedServerOptions(new HttpServerOptions().setPort(8081))
        .setEnabled(true))
      .setEnabled(true);
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));


   PrometheusMeterRegistry registry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();
   registry.config().meterFilter(
       new MeterFilter() {
         @Override
         public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
           return DistributionStatisticConfig.builder()
               .percentilesHistogram(true)
               .build()
               .merge(config);
         }
       });

    vertx.deployVerticle(new SimpleWebServer());
    vertx.deployVerticle(new EventbusConsumer());
    vertx.deployVerticle(new EventbusProducer());
  }
}
