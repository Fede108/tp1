import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class SistemaAlmacenamiento {
    private ArrayList<Casillero> matriz;
    private HashMap<Integer, Casillero> casillerosVisitados;
    private Integer cantPedidos;
    private Integer totalPedidos;

    SistemaAlmacenamiento(Integer totalPedidos)
    {
        matriz = new ArrayList<>(200);
        for (int i = 0; i < 200; i++) {
            matriz.add(new Casillero());
        }
        cantPedidos = 0;
        this.totalPedidos   = totalPedidos;
        casillerosVisitados = new HashMap<>();
    }

    public synchronized Pedido ocuparCasillero() 
    {
       
        Random rnd       = new Random();
        int nroCasillero = rnd.nextInt(200);

        if (casillerosVisitados.size() == 200) 
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        while (casillerosVisitados.containsKey(nroCasillero)) 
        {
            nroCasillero = rnd.nextInt(200);
        }
   
        Casillero casillero = matriz.get(nroCasillero);    
        casillerosVisitados.put(nroCasillero, casillero);

        if (!casillero.estaVacio()) 
        {
            ocuparCasillero();  // Llamada recursiva para intentar llenar otro casillero
        } 
     
        cantPedidos++;
        casillero.ocupar();
        return new Pedido(nroCasillero, cantPedidos);
    }


    public synchronized void desocuparCasillero(Pedido pedido){
        matriz.get(pedido.getCasillero()).desocupar();
        casillerosVisitados.remove(pedido.getCasillero());
        notifyAll();
    }

    public synchronized void setCasilleroFueraServicio(Pedido pedido)
    {
        matriz.get(pedido.getCasillero()).setFueraServicio();
    }

    public Integer getTotalPedidos(){
        return totalPedidos;
    }

}