package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Criptografia.Criptografia;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

//public class ReservaReceiver {
//
//    private static Channel canalPagamentoAprovado;
//    private static String tagPagamentoAprovado;
//
//    public static void inicializaAguardaPagamento() throws Exception {
//        receiverPagamentoAprovado();
//        receiverPagamentoRecusado();
//        receiverBilheteGerado();
//    }
//
//    private static void receiverPagamentoAprovado() throws Exception {
//        final String exchangeName = "pagamento-aprovado";
//        final String queueName = "fila-reserva";
//        final String routingKey = "pagamento";
//
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        channel.exchangeDeclare(exchangeName, "direct");
//        channel.queueDeclare(queueName, true, false, false, null);
//        channel.queueBind(queueName, exchangeName, routingKey);
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            try {
//                String jsonStr = new String(delivery.getBody(), "UTF-8");
//                JSONObject json = new JSONObject(jsonStr);
//
//                String mensagemBase64 = json.getString("mensagem");
//                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);
//
//                boolean verificada = Criptografia.verificaMensagem(json);
//
//                if (verificada) {
//                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
//                    System.out.println("✅ Assinatura verificada. Pagamento de '" + nomeCompleto + "' foi aprovado!");
//                } else {
//                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
//                }
//
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//            } catch (Exception e) {
//                System.err.println("Erro ao processar mensagem: " + e.getMessage());
//                e.printStackTrace();
//                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
//            }
//        };
//
//        // Aqui você salva a tag e o canal
//        tagPagamentoAprovado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
//        canalPagamentoAprovado = channel;
//    }
//
//    public static void pararPagamentoAprovado() throws Exception {
//        if (canalPagamentoAprovado != null && tagPagamentoAprovado != null) {
//            canalPagamentoAprovado.basicCancel(tagPagamentoAprovado);
//            System.out.println("🔴 Listener de pagamento aprovado parado.");
//        }
//    }
//
////    private static void receiverPagamentoAprovado() throws Exception {
////        final String exchangeName = "pagamento-aprovado";
////        final String queueName = "fila-reserva";
////        final String routingKey = "pagamento";
////
////        ConnectionFactory factory = new ConnectionFactory();
////        factory.setHost("localhost");
////        Connection connection = factory.newConnection();
////        Channel channel = connection.createChannel();
////
////        channel.exchangeDeclare(exchangeName, "direct");
////        channel.queueDeclare(queueName, true, false, false, null);
////        channel.queueBind(queueName, exchangeName, routingKey);
////
////        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
////            try {
////                String jsonStr = new String(delivery.getBody(), "UTF-8");
////                JSONObject json = new JSONObject(jsonStr);
////
////                String mensagemBase64 = json.getString("mensagem");
////                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);
////
////                boolean verificada = Criptografia.verificaMensagem(json);
////
////                if (verificada) {
////                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
////                    System.out.println("✅ Assinatura verificada. Pagamento de '" + nomeCompleto + "' foi aprovado!");
////                } else {
////                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
////                }
////
////                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////            } catch (Exception e) {
////                System.err.println("Erro ao processar mensagem: " + e.getMessage());
////                e.printStackTrace();
////                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
////            }
////        };
////
////        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
////    }
//
//
//    private static void receiverPagamentoRecusado() throws Exception {
//        String queueName = "pagamento-recusado";
//
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        channel.queueDeclare(queueName, true, false, false, null);
//
//        channel.basicQos(1);
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            try {
//                String jsonStr = new String(delivery.getBody(), "UTF-8");
//                JSONObject json = new JSONObject(jsonStr);
//
//                String mensagemBase64 = json.getString("mensagem");
//                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);
//
//                boolean verificada = Criptografia.verificaMensagem(json);
//
//                if (verificada) {
//                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
//                    System.out.println("❌ Pagamento de '" + nomeCompleto + "' recusado!");
//                } else {
//                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
//                }
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//            } catch (Exception e) {
//                System.err.println("Erro ao processar mensagem: " + e.getMessage());
//                e.printStackTrace();
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//            }
//        };
//        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
//    }
//
//    private static void receiverBilheteGerado() throws Exception {
//        String queueName = "bilhete-gerado";
//
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        channel.queueDeclare(queueName, true, false, false, null);
//
//        channel.basicQos(1);
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String nomeCompleto = new String(delivery.getBody(), StandardCharsets.UTF_8);
//            System.out.println("✅ Bilhete de '" + nomeCompleto + "' gerado!");
//            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//        };
//        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
//    }
//}

public class ReservaReceiver {

    private static Channel canalPagamentoAprovado;
    private static String tagPagamentoAprovado;

    private static Channel canalPagamentoRecusado;
    private static String tagPagamentoRecusado;

    private static Channel canalBilheteGerado;
    private static String tagBilheteGerado;

    public static void inicializaAguardaPagamento() throws Exception {
        receiverPagamentoAprovado();
        receiverPagamentoRecusado();
        receiverBilheteGerado();
    }

    private static void receiverPagamentoAprovado() throws Exception {
        final String exchangeName = "pagamento-aprovado";
        final String queueName = "fila-reserva";
        final String routingKey = "pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String jsonStr = new String(delivery.getBody(), "UTF-8");
                JSONObject json = new JSONObject(jsonStr);

                String mensagemBase64 = json.getString("mensagem");
                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);

                boolean verificada = Criptografia.verificaMensagem(json);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
                    System.out.println("✅ Assinatura verificada. Pagamento de '" + nomeCompleto + "' foi aprovado!");
                } else {
                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        tagPagamentoAprovado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalPagamentoAprovado = channel;
    }

    private static void receiverPagamentoRecusado() throws Exception {
        final String queueName = "pagamento-recusado";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String jsonStr = new String(delivery.getBody(), "UTF-8");
                JSONObject json = new JSONObject(jsonStr);

                String mensagemBase64 = json.getString("mensagem");
                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);

                boolean verificada = Criptografia.verificaMensagem(json);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
                    System.out.println("❌ Pagamento de '" + nomeCompleto + "' recusado!");
                } else {
                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        tagPagamentoRecusado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalPagamentoRecusado = channel;
    }

    private static void receiverBilheteGerado() throws Exception {
        final String queueName = "bilhete-gerado";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String nomeCompleto = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("✅ Bilhete de '" + nomeCompleto + "' gerado!");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        tagBilheteGerado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalBilheteGerado = channel;
    }

    // Métodos para parar cada listener

    public static void pararPagamentoAprovado() throws Exception {
        if (canalPagamentoAprovado != null && tagPagamentoAprovado != null) {
            canalPagamentoAprovado.basicCancel(tagPagamentoAprovado);
            System.out.println("🔴 Listener de pagamento aprovado parado.");
        }
    }

    public static void pararPagamentoRecusado() throws Exception {
        if (canalPagamentoRecusado != null && tagPagamentoRecusado != null) {
            canalPagamentoRecusado.basicCancel(tagPagamentoRecusado);
            System.out.println("🔴 Listener de pagamento recusado parado.");
        }
    }

    public static void pararBilheteGerado() throws Exception {
        if (canalBilheteGerado != null && tagBilheteGerado != null) {
            canalBilheteGerado.basicCancel(tagBilheteGerado);
            System.out.println("🔴 Listener de bilhete gerado parado.");
        }
    }
}