package ReservaCruzeiros.Marketing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MarketingPublisher {
    public static void publicaPromocao(String promocaoDescricao, String routingKey) throws Exception {
        String exchangeName = "promocoes-destino";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, "direct");

            channel.basicPublish(exchangeName, routingKey, null, promocaoDescricao.getBytes("UTF-8"));
        }
    }
}
