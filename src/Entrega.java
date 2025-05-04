import java.util.Date;
import java.util.Random;

public class Entrega implements Runnable {
    private RegistrodePedidos Registropedidos;


    /**
     * Constructor.
     * @param sistema instancia del sistema de almacenamiento.
     * @param pedidos referencia al registro compartido de pedidos.
     */
    public Entrega( RegistrodePedidos pedidos) {
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

        if (rnd.nextInt(100) < 5) { // 10% de fallas
            pedido.setFallido();
            Registropedidos.addListaFallidos(pedido);
            log("PEDIDO_FALLIDO", pedido);
        } else {
            Registropedidos.addListaEntregados(pedido);
            log("PEDIDO_ENTREGADO", pedido);
        }

        return true;
    }


    /**
     * Registra en consola información sobre acciones realizadas en el sistema.
     * @param accion Descripción de la acción.
     * @param pedido Pedido involucrado (puede ser null).
     */
    private void log(String accion, Pedido pedido) {
        String msg = String.format("%1$tF %1$tT.%1$tL [%2$s] %3$s %4$s",
                new Date(),
                Thread.currentThread().getName(),
                accion,
                (pedido != null ? pedido : ""));
        System.out.println(msg);
    }

 
    /**
     * Ejecuta el hilo de despacho hasta completar la cantidad total de pedidos.
     */
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if ( ! entregaPedido()) {
                break;
            }
        }
    }

}
