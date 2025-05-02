import java.util.Random;

/**
 * Clase Despacho que simula el rol de consumidor.
 * Se encarga de despachar pedidos, liberando casilleros o marcándolos como fallidos.
 */
public class Despacho implements Runnable {
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos Registropedidos;
    int pedidosCompletados;

    /**
     * Constructor.
     * @param sistema instancia del sistema de almacenamiento.
     * @param pedidos referencia al registro compartido de pedidos.
     */
    public Despacho(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos) {
        this.sistema = sistema;
        this.Registropedidos = pedidos;
        this.pedidosCompletados = 0;
    }

    /**
     * Despacha un pedido, liberando el casillero o marcándolo como fuera de servicio en caso de error.
     */
    public void despacharPedido() {
        Pedido pedido = Registropedidos.getListaPreparacion();
        Random rnd = new Random();

        if (rnd.nextInt(100) < 15) { // 15% de fallas
            sistema.setCasilleroFueraServicio(pedido);
            pedido.setFallido();
        } else {
            sistema.desocuparCasillero(pedido);
            Registropedidos.addListaTransito(pedido);
        }
    }

    /**
     * Método sincronizado para incrementar y devolver el número de pedido procesado.
     * @return número del siguiente pedido a procesar o el total si ya se completó.
     */
    public synchronized int siguientePedido() {
        if (pedidosCompletados < sistema.getTotalPedidos()) {
            return pedidosCompletados++;
        } else {
            return pedidosCompletados;
        }
    }

    /**
     * Ejecuta el hilo de despacho hasta completar la cantidad total de pedidos.
     */
    @Override
    public void run() {
        while (siguientePedido() < sistema.getTotalPedidos()) {
            despacharPedido();
        }
    }

    /**
     * Imprime estadísticas y registros de pedidos.
     */
    public void print() {
        System.out.printf("\nCantidad pedidos preparados  %d\n", pedidosCompletados);
        Registropedidos.print();
    }
}