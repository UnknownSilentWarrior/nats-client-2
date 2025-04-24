package io.nats.hello;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.Nats;
import io.nats.client.api.PublishAck;

import java.io.IOException;

// Synchronous Publish Example using the NATS client for Java
public class PublishSync
{
    public static void main(String[] args)
    {
        try (Connection nc = Nats.connect("nats://demo.nats.io"))
        {
            JetStream js = nc.jetStream();

            for(int i = 1; i <= 100; i++) {
                System.out.println("About to publish " + i + " messages");
                PublishAck ps = js.publish("world", ("data"+i).getBytes());
                System.out.println(ps);
            }
        } catch (InterruptedException | IOException | JetStreamApiException e) {
            throw new RuntimeException(e);
        }
    }
}
