# kafka-workshop

A lightweight example project to learn the basics of Kafka. For this workshop, we will be simulating a producer
that produces unoptimised travel routes for cities in the UK. These travel routes would be consumed by a service to be optimised
using various different optimisation algorithm strategies.

Bonus: Monitoring and instrumentation of metrics such as algorithm runtime and route cost improvement is also included.

## Setting up the environment

In order to setup the environment, there are a few pre-requisites:

- NodeJS LTS
- Docker
- Java 17

A docker compose file is available which contains the services needed for kafka, the prometheus metric server
and the grafana metric dashboard. To setup the services simply run:

```bash
docker compose up -d
```

### Setup kafka cluster

Our setup consist of a kafka cluster with 1 broker and a topic `travel_routes` with 3 partitions without replication.

Run the below command to setup the kafka environment.

```
./init-kafka.sh
```

### Start the producer

First you will need to install the Nodejs dependencies. You can do so by running

```
cd producer
npm install
```

The messages are produced by a NodeJS script simulating unoptimised travel routes being published in real time. Run the producer with:

```
node ./producer/producer.js
```

You can test to see if the messages are consumed by using the `consumer.js` script.

```
node ./producer/consumer.js
```

### Start the consumer

In order to start consuming the routes added by the producer, simply run the spring boot application.
The consumed routes are passed to optimisers (depending on which optimisation strategy is set) and the runtime
and cost improvement of the route after compared to the route before is recorded.

There are 3 strategies currently available:

* Greedy - The final travel route is constructed by greedily picking the closest next city until all cities are exhausted.
* [Simulated Annealing](https://en.wikipedia.org/wiki/Simulated_annealing) - An algorithm using a metaheuristic mainly used to solve the TSP problem.
* Multi-layer Simulated Annealing - Simulated annealing but with a slight optimisation where the problem size is broken down into
sub problems and solved in parallel.