package eu.highgeek.common.rabbitmq;

import com.rabbitmq.client.*;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.abstraction.CommonLogger;
import eu.highgeek.common.events.RabbitEvent;
import eu.highgeek.common.objects.config.RabbitConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitListener extends Thread {

    private final CommonLogger logger;

    private volatile boolean running = true;
    private final RabbitConfig rabbitConfig;

    private final String QUEUE_NAME = "mc-queue";
    private final ConnectionFactory factory;

    private Connection connection;
    private Channel channel;

    public RabbitListener(CommonLogger logger, RabbitConfig config) {
        this.rabbitConfig = config;
        this.logger = logger;
        this.factory = new ConnectionFactory();
        factory.setHost(rabbitConfig.getRabbitHost());
        factory.setUsername(rabbitConfig.getRabbitUsername());
        factory.setPassword(rabbitConfig.getRabbitPassword());
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);
    }

    @Override
    public void run() {
        while (running) {
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();

                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                logger.info("RabbitListener: Připojeno k RabbitMQ, čekám na zprávy.");

                DeliverCallback callback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    CommonMain.postRabbitEvent(new RabbitEvent(delivery));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                };

                CancelCallback cancelCallback = consumerTag -> {
                    logger.warn("Consumer byl zrušen: " + consumerTag);
                };

                channel.basicConsume(QUEUE_NAME, false, callback, cancelCallback);

                // Wait until interrupted or stopped
                while (running && connection.isOpen()) {
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                logger.warn("RabbitMQ connection error: " + e.getMessage());
                try {
                    Thread.sleep(5000); // pauza před pokusem o reconnect
                } catch (InterruptedException ignored) {}
            } finally {
                cleanup();
            }
        }
        logger.info("RabbitListener: Shutdown.");
    }

    public void stopConsumer() {
        running = false;
        cleanup();
        this.interrupt();
    }

    private void cleanup() {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            if (connection != null && connection.isOpen()) connection.close();
        } catch (IOException | TimeoutException ignored) {}
    }
}
