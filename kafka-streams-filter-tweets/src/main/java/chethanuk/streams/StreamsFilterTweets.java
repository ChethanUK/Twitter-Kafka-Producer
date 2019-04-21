package chethanuk.streams;

import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.logging.Logger;

public class StreamsFilterTweets {
    private static final Logger logger = Logger.getLogger(StreamsFilterTweets.class.getName());
    private static JsonParser jsonParser = new JsonParser();

    public static void main(String[] args) {
        logger.info("Start of Main");
        // Create Properties
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-twitter-streams"); // id for streams
        // String has values and Keys
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName()); // String has Keys
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName()); // String has values


        // Create Topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // Define input topic
        KStream<String, String> inputKStream = streamsBuilder.stream("tw_tweets");

        KStream<String, String> filteredKStream = inputKStream.filter(
                // Filter
                (k, v) ->
                        // Filter for tweets tweeted by users having followers more than 10K
                        extractUserFollowersFromTweet(v) > 10000
        );
        logger.info("Filtering");

        // Send stream to topic
        filteredKStream.to("important_tweets");

        // Build Topology
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);

        logger.info("Starting Streams");
        // Start the Stream
        kafkaStreams.start();

    }

    private static Integer extractUserFollowersFromTweet(String tweetJson) {
        // gson library
        try {
            return jsonParser.parse(tweetJson)
                    .getAsJsonObject()
                    .get("user")
                    .getAsJsonObject()
//                    .get("FollowersCount")
                    .get("followers_count")
                    .getAsInt();
        } catch (NullPointerException e) {
            return 0;
        }

    }
}
