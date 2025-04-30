public class Preparacion implements Runnable{
    private SistemaAlmacenamiento sistema;
    private Pedido pedido;
    private boolean pedidosCompletados;

    public Preparacion(SistemaAlmacenamiento sistema){
        this.sistema = sistema;
        pedidosCompletados = false;
    }

    public void llenarRegistro(){
        try {
            pedido = sistema.ocuparCasillero();
        } catch (Throwable e) {
           System.out.println("pedidos completados"); 
           pedidosCompletados = true;
        }
        System.out.println(pedido.id);
    }

    @Override
    public void run() {
       while (!pedidosCompletados) {
        llenarRegistro();
       } 
    }
}