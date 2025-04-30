import java.util.Random;

public class Despacho implements Runnable{
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos     Registropedidos;
    int pedidosCompletados;

    public Despacho(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos){
        this.sistema    = sistema;
        Registropedidos = pedidos;
        pedidosCompletados = 0;
    }

    public void despacharPedido(){
        Pedido pedido = Registropedidos.getListaPreparacion();
        if (pedido != null) {
            Random rnd    = new Random();
            if (rnd.nextInt(100) < 15) {
                sistema.setCasilleroFueraServicio(pedido);
                pedido.setFallido();
            }else{
                Registropedidos.addListaTransito(pedido);
                sistema.desocuparCasillero(pedido);
            }
        }
    }
        

    @Override
    public void run() {
        while (pedidosCompletados<5) {
            despacharPedido();
            pedidosCompletados++;
        }

        System.out.println(pedidosCompletados);
        Registropedidos.print();
    }
}
