package io.nats.hello;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.Nats;
import io.nats.client.api.PublishAck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// Asynchronous Publish Example using the NATS client for Java
public class PublishAsync
{
    public static void main(String[] args)
    {
        try (Connection nc = Nats.connect("nats://demo.nats.io")) {
            JetStream js = nc.jetStream();

            List<CompletableFuture<PublishAck>> futures = new ArrayList<>();
            for(int i = 0; i <= 100; i++) {
                System.out.println("About to publish " + i);
                CompletableFuture<PublishAck> future = js.publishAsync("world", ("data"+i).getBytes());
                futures.add(future);
            }

            // Sleep 5 seconds to finish publishing all the data
            Thread.sleep(5000);

            for(CompletableFuture<PublishAck> future : futures) {
                PublishAck ack = future.get(1, TimeUnit.SECONDS);
                System.out.println(ack);
            }

        } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
