package ReservaCruzeiros.Service;

import ReservaCruzeiros.Bilhete.BilheteReceiver;
import ReservaCruzeiros.Pagamento.PagamentoReceiver;
import ReservaCruzeiros.Reserva.ReservaReceiver;

public class Service {
    public void inicializaReceivers() throws Exception {
        ReservaReceiver.inicializaAguardaPagamento();
        PagamentoReceiver.inicializaAguardaNovaReserva();
        BilheteReceiver.inicializaAguardaPagamentoAprovado();
    }

    public void limparConsole() {
        try {
            String sistema = System.getProperty("os.name");
            if (sistema.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Não foi possível limpar o console");
        }
    }
}
