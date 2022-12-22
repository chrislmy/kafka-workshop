const { Kafka, logLevel, PartitionAssigners } = require('kafkajs');

const kafka = new Kafka({
  logLevel: logLevel.INFO,
  clientId: 'kafka-workshop-test-consumer',
  brokers: ['localhost:9092'],
  retry: {
    initialRetryTime: 100,
    retries: 8,
  },
});

const consumer = kafka.consumer({
  partitionAssigners: [PartitionAssigners.roundRobin],
  groupId: 'workshop-test-consumer-group' 
});

const run = async () => {
  await consumer.connect();
  await consumer.subscribe({ topic: 'travel_routes', fromBeginning: true });
  await consumer.run({
    partitionsConsumedConcurrently: 3,
    eachMessage: async ({ topic, partition, message }) => {
      const prefix = `${topic}[${partition} | ${message.offset}] / ${message.timestamp}`;
      console.log(`- ${prefix} ${message.key}`);
    },
  });
};

run().catch(e => console.log(`Error in consumer: ${e.message}`, e));

const errorTypes = ['unhandledRejection', 'uncaughtException'];
const signalTraps = ['SIGTERM', 'SIGINT', 'SIGUSR2'];

errorTypes.forEach(type => {
  process.on(type, async e => {
    try {
      console.log(`Error occured with type [${type}], disconnecting consumer`);
      await consumer.disconnect();
      process.exit(0);
    } catch (_) {
      process.exit(1);
    }
  })
})

signalTraps.forEach(type => {
  process.once(type, async () => {
    try {
      console.log(`Process killed [${type}], disconnecting consumer`);
      await consumer.disconnect();
    } finally {
      process.kill(process.pid, type);
    }
  })
})