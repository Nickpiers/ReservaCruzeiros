package ReservaCruzeiros.Pagamento;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class PagamentoReceiver {
    public static void inicializaAguardaNovaReserva() throws Exception {
        final String exchangeName = "reserva-criada";
        final String queueName = "fila-pagamento-receiver";
        final String routingKey = "pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String nomeCompleto = new String(delivery.getBody(), StandardCharsets.UTF_8);
            try {
                aguardaAprovacao(nomeCompleto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }

    private static void aguardaAprovacao(String nomeCompleto) throws Exception {
        String queueName = "aprova-pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
            boolean aprovado = Boolean.parseBoolean(body);
            String aprovacao = aprovado ? "aprovado" : "recusado";

            try {
                PagamentoPublisher pagamentoPublisher = new PagamentoPublisher();
                pagamentoPublisher.processaPagamento(nomeCompleto, aprovacao);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }
}
