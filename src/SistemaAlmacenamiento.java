import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class SistemaAlmacenamiento {
    private ArrayList<Casillero> matriz;
    private HashMap<Integer, Casillero> casillerosVisitados;
    private Integer cantPedidos;
    private Integer totalPedidos;

    SistemaAlmacenamiento(Integer totalPedidos)
    {
        matriz = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            matriz.add(new Casillero());
        }
        cantPedidos = 0;
        this.totalPedidos   = totalPedidos;
        casillerosVisitados = new HashMap<>();
    }

    public synchronized Pedido ocuparCasillero() 
    {
        int nroCasillero;
        Casillero casillero;

        while (casillerosVisitados.size() == 5) 
        {
            log("CASILLEROS_LLENOS", null);
        //    System.out.printf("Thread '%s': casilleros llenos \n", Thread.currentThread().getName());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        do {
            nroCasillero = new Random().nextInt(5);
            casillero    = matriz.get(nroCasillero);
        } while (casillerosVisitados.containsKey(nroCasillero) || !casillero.estaVacio());
        

        casillerosVisitados.put(nroCasillero, casillero);
        casillero.ocupar();

        
     //   System.out.printf("Thread '%s': casillero ocupado \n", Thread.currentThread().getName());
        Pedido pedido = new Pedido(nroCasillero, ++cantPedidos);
        log("OCUPAR_CASILLERO  ", pedido);
        return pedido;
    }


    public synchronized void desocuparCasillero(Pedido pedido){
        matriz.get(pedido.getCasillero()).desocupar();
        casillerosVisitados.remove(pedido.getCasillero());
        log("CASILLERO_LIBERADO", pedido);
     //   System.out.printf("Thread '%s': casilleros desocupado \n", Thread.currentThread().getName());
        notifyAll();
    }

    public synchronized void setCasilleroFueraServicio(Pedido pedido)
    {
        matriz.get(pedido.getCasillero()).setFueraServicio();
    }

    public Integer getTotalPedidos(){
        return totalPedidos;
    }

    private void log(String accion, Pedido pedido) {
        String msg = String.format("%1$tF %1$tT.%1$tL [%2$s] %3$s %4$s",
            new Date(),
            Thread.currentThread().getName(),
            accion,
            (pedido != null ? pedido : ""));
        System.out.println(msg);
    }
}