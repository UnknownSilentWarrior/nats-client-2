package io.nats.hello;

import io.nats.client.*;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import io.nats.client.support.JsonUtils;

import java.io.IOException;
import java.time.Duration;

public class StreamSimple
{
    public static void main(String[] args)
    {
        try(Connection nc = Nats.connect("nats://demo.nats.io"))
        {
            JetStreamManagement jsm = nc.jetStreamManagement();

            // Build the configuration
            StreamConfiguration streamConfig = StreamConfiguration.builder()
                    .name("hello")
                    .storageType(StorageType.Memory)
                    .subjects("world")
                    .build();

            // Create the stream
            StreamInfo streamInfo = jsm.addStream(streamConfig);

            JsonUtils.printFormatted(streamInfo);

            JetStream js = nc.jetStream();
            js.publish("world", "one-data".getBytes());
            js.publish("world", "two-data".getBytes());

            JetStreamSubscription sub = js.subscribe("world");
            Message m = sub.nextMessage(Duration.ofSeconds(1));
            System.out.println("Message: " + m.getSubject() + " " + new String(m.getData()));
            JsonUtils.printFormatted(m.metaData());

            m = sub.nextMessage(Duration.ofSeconds(1));
            System.out.println("Message2: " + m.getSubject() + " " + new String(m.getData()));
            JsonUtils.printFormatted(m.metaData());

        } catch (InterruptedException | IOException | JetStreamApiException e) {
            throw new RuntimeException(e);
        }
    }
}
