package ReservaCruzeiros;

import ReservaCruzeiros.Pagamento.AprovaPagamentoAdmin;

//java -jar adminPagamento.jar
public class AdminPagamento {
    public static void main(String[] argv) throws Exception {
        System.out.println("Aguardando pagamentos.....");
        AprovaPagamentoAdmin.recebeReservaCriada();
    }
}
