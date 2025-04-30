package io.nats.hello;

import io.nats.client.*;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import io.nats.client.support.JsonUtils;

import java.io.IOException;
import java.time.Duration;

public class StreamSpecificSubSpecific
{
    public static void main(String[] args)
    {
        try (Connection nc = Nats.connect("nats://demo.nats.io")) {
            JetStreamManagement jsm = nc.jetStreamManagement();

            // Build the configuration
            StreamConfiguration streamConfig = StreamConfiguration.builder()
                    .name("hello2")
                    .storageType(StorageType.Memory)
                    .subjects("world.one", "world.two")
                    .build();

            // Create a stream
            StreamInfo streamInfo = jsm.addStream(streamConfig);

            JsonUtils.printFormatted(streamInfo);

            JetStream js = nc.jetStream();
            js.publish("world.one", "one-data".getBytes());
            js.publish("world.two", "two-data".getBytes());

            //-------------- Subscription ------------------
            JetStreamSubscription sub = js.subscribe("world.one");
            Message m = sub.nextMessage(Duration.ofSeconds(1));
            System.out.println("Message: " + m.getSubject() + " " + new String(m.getData()));
            JsonUtils.printFormatted(m.metaData());


            sub = js.subscribe("world.two");
            m = sub.nextMessage(Duration.ofSeconds(1));
            System.out.println("Message: " + m.getSubject() + " " + new String(m.getData()));
            JsonUtils.printFormatted(m.metaData());

        } catch (InterruptedException | IOException | JetStreamApiException e) {
            throw new RuntimeException(e);
        }
    }
}
