package ReservaCruzeiros.Service;

import ReservaCruzeiros.Criptografia.Criptografia;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQMetodos {
    public static void publisherQueue(String queueName, String mensagem) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);

            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes("UTF-8"));
        }
    }
}
