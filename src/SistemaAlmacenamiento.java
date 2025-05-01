import java.util.ArrayList;
import java.util.Random;


public class SistemaAlmacenamiento {
    private ArrayList<Casillero> Matriz;
    private Integer cantPedidos;
    private Integer totalPedidos;

    SistemaAlmacenamiento(Integer totalPedidos)
    {
        Matriz = new ArrayList<>(200);
        for (int i = 0; i < 200; i++) {
            Matriz.add(new Casillero());
        }
        cantPedidos = 0;
        this.totalPedidos = totalPedidos;
    }

    public synchronized Pedido ocuparCasillero(){
        return _ocuparCasillero(0,-1);
    }

    public synchronized Pedido _ocuparCasillero(int contador, int nroCasilleroViejo) 
    {
       
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
            contador++;
            _ocuparCasillero(contador, nroCasilleroNuevo);  // Llamada recursiva para intentar llenar otro casillero
        }

        cantPedidos++;
        casillero.ocupar();
        return new Pedido(nroCasilleroNuevo, cantPedidos);
    }


    public synchronized void desocuparCasillero(Pedido pedido){
        Matriz.get(pedido.getCasillero()).desocupar();
        notifyAll();
    }

    public synchronized void setCasilleroFueraServicio(Pedido pedido)
    {
        Matriz.get(pedido.getCasillero()).setFueraServicio();
    }

    public Integer getTotalPedidos(){
        return totalPedidos;
    }

}