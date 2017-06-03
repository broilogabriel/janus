# Janus Project

`sbt app/docker:publishLocal`

`docker run --rm --network janus broilogabriel/app:17.5.0`

`docker run --rm --network janus ches/kafka kafka-topics.sh --create --topic janus --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181`

`docker run --rm --interactive --network janus ches/kafka:0.10.2.0 kafka-console-producer.sh --topic janus --broker-list kafka:9092`

`docker run --rm --network janus ches/kafka:0.10.2.0 kafka-console-consumer.sh --topic janus --from-beginning --bootstrap-server kafka:9092`