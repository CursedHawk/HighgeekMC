package eu.highgeek.common.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import eu.highgeek.common.CommonMain;
import eu.highgeek.common.objects.config.RabbitConfig;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class RabbitProducer {

    private final RabbitConfig config = new RabbitConfig();

    private Connection connection;
    private final ConnectionFactory factory;

    public RabbitProducer(){
        this.factory = new ConnectionFactory();
        factory.setHost(config.getRabbitHost());
        factory.setPassword(config.getRabbitPassword());
        factory.setUsername(config.getRabbitUsername());
        try{
            this.connection = factory.newConnection();
        }catch (Exception e){
            CommonMain.getLogger().severe("Connection to Rabbit cannot be established!\n" + e);
        }
    }

    //todo test
    public void publishMessage(String queName, String message) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queName, false, false, false, null);
        channel.basicPublish("", queName, null, message.getBytes());
    }
}
