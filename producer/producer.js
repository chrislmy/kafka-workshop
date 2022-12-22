const { Kafka, logLevel } = require('kafkajs');
const { v4: uuidv4 } = require('uuid');
const { generateRoutes, getCityData } = require('./lib/routeGenerator');

const TOPIC_NAME = 'travel_routes';
const MAX_BATCH_SIZE = 5; // Max number of messages per batch
const MAX_ROUTE_SIZE = 10; // Max number of cities in a route
const MAX_MESSAGE_INTERVAL_SECONDS = 5; // Max interval length in seconds between messages sent
const CITY_DATA_FILENAME = 'uk-cities-data';

const kafka = new Kafka({
  logLevel: logLevel.INFO,
  clientId: 'kafka-workshop-producer',
  brokers: ['localhost:9092'],
  retry: {
    initialRetryTime: 100,
    retries: 8,
  },
});

const producer = kafka.producer();

const messageBatch = async (cities) => {
  const routes = await generateRoutes(MAX_BATCH_SIZE, MAX_ROUTE_SIZE, cities);
  const messages = routes
    .map(route => {
      const routeId = uuidv4();
      console.log(`Producing message with route id: ${routeId}`);

      return {
        key: routeId,
        value: JSON.stringify({
          routeId,
          route,
        }),
      };
    });

  return messages;
};

const produceMessages = async (cities) => {
  const timeoutInSeconds = Math.floor(Math.random() * MAX_MESSAGE_INTERVAL_SECONDS) + 1;
  console.log('======= Producing new batch of messages =======');

  try {
    await producer.send({
      topic: TOPIC_NAME,
      messages: await messageBatch(cities),
    });
  } catch (error) {
    console.log(`Error when producing message: ${error}`);
  }

  setTimeout(() => {
    produceMessages(cities)
  }, timeoutInSeconds * 1000);
};

const run = async () => {
  const cities = await getCityData(CITY_DATA_FILENAME);
  await producer.connect();
  await produceMessages(cities);
};

run()
  .catch(err => {
    console.log(`Error when producing messages: ${err}`);
  });

// Gracefully disconnect producer
const errorTypes = ['unhandledRejection', 'uncaughtException'];
const signalTraps = ['SIGTERM', 'SIGINT', 'SIGUSR2'];

errorTypes.forEach(type => {
  process.on(type, async () => {
    try {
      console.log(`Error occured with type [${type}], disconnecting producer`);
      await producer.disconnect();
      process.exit(0);
    } catch (_) {
      process.exit(1);
    }
  });
});

signalTraps.forEach(type => {
  process.once(type, async () => {
    try {
      console.log(`Process killed [${type}], disconnecting producer`);
      await producer.disconnect();
    } finally {
      process.kill(process.pid, type);
    }
  })
});
