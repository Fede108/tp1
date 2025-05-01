import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class SistemaAlmacenamiento {
    private ArrayList<Casillero> matriz;
    private HashMap<Integer, Casillero> casillerosVisitados;
    private Integer cantPedidos;
    private Integer totalPedidos;

    private static final int N_CASILLEROS = 8; 

    SistemaAlmacenamiento(Integer totalPedidos)
    {
        matriz = new ArrayList<>(N_CASILLEROS);
        for (int i = 0; i < N_CASILLEROS; i++) {
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

        while (casillerosVisitados.size() == N_CASILLEROS) 
        {
            log("CASILLEROS_LLENOS", null);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        do {
            nroCasillero = new Random().nextInt(N_CASILLEROS);
            casillero    = matriz.get(nroCasillero);
        } while (casillerosVisitados.containsKey(nroCasillero) || !casillero.estaVacio() || casillero.estaFueraServicio());
        

        casillerosVisitados.put(nroCasillero, casillero);
        casillero.ocupar();

        Pedido pedido = new Pedido(nroCasillero, ++cantPedidos);
        log("OCUPAR_CASILLERO  ", pedido);
        return pedido;
    }


    public synchronized void desocuparCasillero(Pedido pedido){
        matriz.get(pedido.getCasillero()).desocupar();
        casillerosVisitados.remove(pedido.getCasillero());
        log("CASILLERO_LIBERADO", pedido);
     //   System.out.printf("Thread '%s': casilleros desocupado \n", Thread.currentThread().getName());
        notify();
    }

    public synchronized void setCasilleroFueraServicio(Pedido pedido)
    {
        matriz.get(pedido.getCasillero()).setFueraServicio();
      //  casillerosVisitados.remove(pedido.getCasillero());
        log("PEDIDO_FALLIDO    ", pedido);
      //  notify();
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