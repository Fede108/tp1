import java.util.Random;

public class Entrega implements Runnable {
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos Registropedidos;


    /**
     * Constructor.
     * @param sistema instancia del sistema de almacenamiento.
     * @param pedidos referencia al registro compartido de pedidos.
     */
    public Entrega(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos) {
        this.sistema = sistema;
        this.Registropedidos = pedidos;
    }

    /**
     * Entrega un pedido, liberando el casillero o marcándolo como fuera de servicio en caso de error.
     */
    public boolean entregaPedido() {
        Pedido pedido = Registropedidos.getListaTransito();
        if (pedido.pedidoPoison()) {
            return false;
        }

        Random rnd = new Random();

        if (rnd.nextInt(100) < 2) { // 10% de fallas
            sistema.setCasilleroFueraServicio(pedido);
            pedido.setFallido();
            Registropedidos.addListaFallado(pedido);
        } else {
            Registropedidos.addListaEntregados(pedido);
        }

        return true;
    }

 
    /**
     * Ejecuta el hilo de despacho hasta completar la cantidad total de pedidos.
     */
    @Override
    public void run() {
        while (true){
            if ( ! entregaPedido()) {
                break;
            }
        }
    }

}
