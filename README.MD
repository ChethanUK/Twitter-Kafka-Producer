## Twiter Kafka Producer

Read data from Twitter using hosebird and push data into Kafka topic

## Kafka Twitter connector
  
  cd into directory: `cd /src/connect/kafka-connect-twitter-0.2.32`
  
  1. Download the latest version from https://www.confluent.io/connector/kafka-connect-twitter/
  
      - Extract the content into a folder
      
      - Define connect-standalone properties
      
      - Define twitter.propteties
  
  2. Create Kafka topics
      - twitter-status-connect
          ```
          kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 5 --topic twitter-status-connect
          ``` 
      - twitter-deletes-connect
          ```
          kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 5 --topic twitter-deletes-connect
          ```
  3. Run connect standalone
      ``` 
      cd ./kafka-connect
      
      connect-standalone connect-standalone.properties twitter.properties
      ```
      
### Partitions
  - Each partitions can handle a throughtput of few MB/s
  - Guidlines:
    - For Small Clusters (less than 7 brokers):
        `Number of partitions = 2 * number of brokers`
    
### Replication factor
    - Should be atleast 2
    - Never set it to 1 in production, get better broker if performance degrade over time
            
              