package ReservaCruzeiros;

import ReservaCruzeiros.Menu.Menu;
import ReservaCruzeiros.Service.Service;

public class Cliente {

    public static void main(String[] argv) throws Exception {
        Menu menu = new Menu();
        Service service = new Service();

        service.inicializaReceivers();
        menu.start();
    }

}
