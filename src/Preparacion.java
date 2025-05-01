
public class Preparacion implements Runnable{
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos     Registropedidos;
    private int pedidosCompletados;

    public Preparacion(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos){
        this.sistema       = sistema;
        Registropedidos    = pedidos;
        pedidosCompletados = 0;
    }

    public void prepararPedido(){
        Pedido pedido = sistema.ocuparCasillero();
        Registropedidos.addListaPreparacion(pedido);   
    }

    public synchronized int siguientePedido() {
        return ++pedidosCompletados;
    }

    @Override
    public void run() {
       while (siguientePedido()<sistema.getTotalPedidos()) {
            prepararPedido();
       } 
    }
}