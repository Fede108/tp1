import java.util.ArrayList;
import java.util.Random;


public class SistemaAlmacenamiento {
    private ArrayList<Casillero> Matriz;
    private Integer cantPedidos;

    SistemaAlmacenamiento()
    {
        Matriz = new ArrayList<>(200);
        for (int i = 0; i < 200; i++) {
            Matriz.add(new Casillero());
        }
        cantPedidos = 0;
    }

    public synchronized Pedido ocuparCasillero(int contador, int nroCasilleroViejo) 
    {
        cantPedidos++;
        Random rnd            = new Random();
        int nroCasilleroNuevo = rnd.nextInt(200);

        while (nroCasilleroNuevo == nroCasilleroViejo) 
        {
            nroCasilleroNuevo = rnd.nextInt(200);
        }

        Casillero casillero = Matriz.get(nroCasilleroNuevo);

        if (contador >= 200) {

        }
        if (!casillero.estaVacio()) 
        {
            
            ocuparCasillero(contador, nroCasilleroNuevo);  // Llamada recursiva para intentar llenar otro casillero
        }
        casillero.ocupar();
        return new Pedido(nroCasilleroNuevo, cantPedidos);

    }


    public synchronized void desocuparCasillero(Pedido pedido){
        Matriz.get(pedido.getCasillero()).desocupar();
    }

    public synchronized void setCasilleroFueraServicio(Pedido pedido)
    {
        Matriz.get(pedido.getCasillero()).setFueraServicio();
    }
}