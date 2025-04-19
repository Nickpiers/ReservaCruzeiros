package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Criptografia.Criptografia;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class PagamentoPublisher {

    public void processaPagamento(String nomeCompleto, String pagamento) throws Exception {
        System.out.println("Processando pagamento...");

        if (pagamento.equals("aprovado")) {
            aprovaPagamento(nomeCompleto);
        }
        else {
            recusaPagamento(nomeCompleto);
        }
    };

    private static void aprovaPagamento(String nomeCompleto) throws Exception {
        String exchangeName = "pagamento-aprovado";
        String routingKey = "pagamento";
        String mesangemCriptografada = Criptografia.criptografaMensagem(nomeCompleto);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, "direct");

            channel.basicPublish(exchangeName, routingKey, null, mesangemCriptografada.getBytes("UTF-8"));
        }
    }

    private static void recusaPagamento(String nomeCompleto) throws Exception {
        String queueName = "pagamento-recusado";

        String mensagemCriptografada = Criptografia.criptografaMensagem(nomeCompleto);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);

            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagemCriptografada.getBytes("UTF-8"));
        }
    }
}
