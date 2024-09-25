# Vert.x-Micrometer-Metrics


== Launching the example from the command line

First, build the fat jar:

[source,bash]
----
mvn clean package
----

Then run any of these Main classes:

=== Prometheus

link:https://prometheus.io/docs/prometheus/latest/getting_started/[Check here] for Prometheus getting started guide.

You need to configure the Prometheus server to scrape `localhost:8081`.

[source,yaml]
----
  - job_name: 'vertx-8081'
    static_configs:
      - targets: ['localhost:8081']
----

[TIP]
====
To run a pre-configured Prometheus server on your machine with Docker, go to this example directory and run:

[source,bash]
----
docker run --network host \
-v ${PWD}/prometheus:/etc/prometheus \
-it prom/prometheus
----
====

To start the example from the command line:

[source,bash]
----
java -cp target/micrometer-metrics-examples-4.5.9-fat.jar io.vertx.example.micrometer.prometheus.Main
----

By default, histogram sampling is disabled.
You can enable it manually in the Micrometer registry, as shown in commented code in class
link:src/main/java/io/vertx/example/micrometer/prometheus/Main.java[`io.vertx.example.micrometer.prometheus.Main`]:

[source,java]
----
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
----

See also link:https://micrometer.io/docs/concepts#_histograms_and_percentiles[Micrometer documentation].

=== Prometheus with endpoint bound to existing server

[source,bash]
----
java -cp target/micrometer-metrics-examples-4.5.9-fat.jar io.vertx.example.micrometer.prometheus.MainWithBoundPrometheus
----

You need to configure the Prometheus server to scrape `localhost:8080`.

[source,yaml]
----
  - job_name: 'vertx-8080'
    static_configs:
      - targets: ['localhost:8080']
----

=== InfluxDB

This sample application expects an InfluxDB server running on localhost, port 8086, without authentication.
For quick setup, you can run it with this docker command:

[source,bash]
----
docker run -p 8086:8086 influxdb
----

Start the application:

[source,bash]
----
java -cp target/micrometer-metrics-examples-4.5.9-fat.jar io.vertx.example.micrometer.influxdb.Main
----

=== JMX

[source,bash]
----
java -cp target/micrometer-metrics-examples-4.5.9-fat.jar io.vertx.example.micrometer.jmx.Main
----
Metrics will be available under domain `metrics`.

== Triggering some workload

You can trigger some workload to see the impact on HTTP server metrics:

[source,bash]
----
while true
do curl http://localhost:8080/
    sleep .8
done
----

== View in Grafana

Metrics can be observed from https://grafana.com/docs/grafana/latest/guides/getting_started/[Grafana].

[TIP]
====
To run a Grafana server on your machine with Docker, go to this example directory and run:

[source,bash]
----
docker run --network host \
-it grafana/grafana
----
====

These dashboards track some HTTP server and event bus metrics:

* link:grafana/Vertx-Prometheus.json[for Prometheus]
* link:grafana/Vertx-InfluxDB.json[for InfluxDB]

.HTTP server metrics
image:grafana/http-server-metrics.png[HTTP server metrics]

.Event bus metrics
image:grafana/eventbus-metrics.png[Event bus metrics]

