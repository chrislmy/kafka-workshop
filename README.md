# kafka-workshop

A lightweight example project to learn the basics of Kafka. For this workshop, we will be simulating a producer
that produces unoptimised travel routes for cities in the UK. These travel routes would be consumed by a service to be optimised.

## Setting up the environment

In order to setup the environment, there are a few pre-requisites:

- NodeJS LTS
- Docker
- Java 17

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