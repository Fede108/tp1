public class Preparacion implements Runnable{
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos     Registropedidos;
    private Pedido pedido;
    private int pedidosCompletados;

    public Preparacion(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos){
        this.sistema       = sistema;
        Registropedidos    = pedidos;
        pedidosCompletados = 0;
    }

    public void llenarRegistro(){
        pedido = sistema.ocuparCasillero();
        Registropedidos.addListaPreparacion(pedido);   
    }

    @Override
    public void run() {
       while (pedidosCompletados<8) {
        llenarRegistro();
        pedidosCompletados++;
       } 
    }
}