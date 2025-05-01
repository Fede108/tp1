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
       
        Random rnd       = new Random();
        int nroCasillero = rnd.nextInt(5);

        while (casillerosVisitados.size() == 5) 
        {
            System.out.printf("Thread '%s': casilleros llenos \n", Thread.currentThread().getName());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        while (casillerosVisitados.containsKey(nroCasillero)) 
        {
            nroCasillero = rnd.nextInt(5);
        }
   
        Casillero casillero = matriz.get(nroCasillero);    
        casillerosVisitados.put(nroCasillero, casillero);

        if (!casillero.estaVacio()) 
        {
            ocuparCasillero();  // Llamada recursiva para intentar llenar otro casillero
          
        } 
     
        cantPedidos++;
        casillero.ocupar();
        System.out.printf("Thread '%s': casillero ocupado \n", Thread.currentThread().getName());
        return new Pedido(nroCasillero, cantPedidos);
    }


    public synchronized void desocuparCasillero(Pedido pedido){
        matriz.get(pedido.getCasillero()).desocupar();
        casillerosVisitados.remove(pedido.getCasillero());
        System.out.printf("Thread '%s': casilleros desocupado \n", Thread.currentThread().getName());
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