package ReservaCruzeiros.Marketing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MarketingReceiver {

    public static void inscreveNaPromocao(String promocaoNome, String queueName, String routingKey) throws Exception {
        final String exchangeName = "promocoes-destino";

        System.out.println("Inscrito para a promoção: " + promocaoNome + "!");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String promocaoDescricao = new String(delivery.getBody(), "UTF-8");
            System.out.println("!!! PROMOÇÃO RECEBIDA !!!");
            System.out.println(promocaoDescricao);
            System.out.println("--------------------------");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }
}
