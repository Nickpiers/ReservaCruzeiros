package ReservaCruzeiros.Reserva;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ReservaPublisher {
    public static void novaReserva(ReservaDto reserva) throws Exception {
        String exchangeName = "reserva-criada";
        String routingKey = "pagamento";
        String nomeCompleto = reserva.getNomeCompleto();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, "direct");

            String nomeJunto = nomeCompleto.replaceAll("\\s+", "");
            System.out.println("---------------------------");
            System.out.println("Link para pagamento: https://ReservaCruzeiros.com/reserva/pagamento/" + nomeJunto);

            channel.basicPublish(exchangeName, routingKey, null, nomeCompleto.getBytes("UTF-8"));
        }

    };
}
