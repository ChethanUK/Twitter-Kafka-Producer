
# Kafka Streams

- Create the topic :"important_tweets"
    kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 5 --topic important_tweets
    
    kafka-console-consumer --bootstrap-server localhost:9092 --topic important_tweets
        