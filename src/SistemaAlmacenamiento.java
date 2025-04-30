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

    public Pedido ocuparCasillero() throws Throwable
    {
        Random rnd = new Random();
        cantPedidos++;
        if (cantPedidos == 500) {
            throw new Exception();
        }
    

        while(true){    
            int nroCasillero = rnd.nextInt(200);
            Casillero casillero = Matriz.get(nroCasillero);

            if (casillero.estaVacio()) 
            {
                casillero.ocupar();
                return new Pedido(nroCasillero, cantPedidos);
            }
        }

    }

    public void desocuparCasillero(Pedido pedido){
        Matriz.get(pedido.getCasillero()).desocupar();
    }

    public void setCasilleroFueraServicio()
    {

    }
}