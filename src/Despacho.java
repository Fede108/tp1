import java.util.Random;

public class Despacho implements Runnable{
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos     Registropedidos;
    int pedidosCompletados;

    public Despacho(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos){
        this.sistema       = sistema;
        Registropedidos    = pedidos;
        pedidosCompletados = 0;
    }

    public void despacharPedido(){
        Pedido pedido = Registropedidos.getListaPreparacion();
        Random rnd    = new Random();
        
        if (rnd.nextInt(100) < 15) {
            sistema.setCasilleroFueraServicio(pedido);
            pedido.setFallido();
        }else{
            Registropedidos.addListaTransito(pedido);
            sistema.desocuparCasillero(pedido);
        }
    }

        
    public synchronized int siguientePedido() {
        if (pedidosCompletados<sistema.getTotalPedidos()) {
            return ++pedidosCompletados;
        }
        else{
            return pedidosCompletados;
        }
    }


    @Override
    public void run() {
        while (siguientePedido()<sistema.getTotalPedidos()) {
            despacharPedido();
        }
  
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

     //   print();
    }

    public void print(){
        System.out.println(pedidosCompletados);
        Registropedidos.print();
    }
}
