package ReservaCruzeiros.Bilhete;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class BilhetePublisher {
    public void geraBilhete(String nomeCompleto) throws Exception {
        String queueName = "bilhete-gerado";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);

            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, nomeCompleto.getBytes("UTF-8"));
        }
    };
}
