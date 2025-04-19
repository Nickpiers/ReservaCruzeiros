package ReservaCruzeiros.Bilhete;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class BilheteReceiver {
    private static final String EXCHANGE_NAME = "pagamento-aprovado";
    private static final String QUEUE_NAME = "fila-bilhete";
    private static final String ROUTING_KEY = "pagamento";

    public static void inicializaAguardaPagamentoAprovado() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String jsonStr = new String(delivery.getBody(), "UTF-8");

                JSONObject json = new JSONObject(jsonStr);
                String mensagemBase64 = json.getString("mensagem");
                String assinaturaBase64 = json.getString("assinatura");

                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);
                byte[] assinaturaBytes = Base64.getDecoder().decode(assinaturaBase64);

                byte[] keyBytes = Files.readAllBytes(Paths.get("public.key"));
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                PublicKey publicKey = keyFactory.generatePublic(keySpec);

                Signature sig = Signature.getInstance("SHA1withDSA");
                sig.initVerify(publicKey);
                sig.update(mensagemBytes);

                boolean verificada = sig.verify(assinaturaBytes);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
                    BilhetePublisher bilhetePublisher = new BilhetePublisher();
                    bilhetePublisher.geraBilhete(nomeCompleto);
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}
