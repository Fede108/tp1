import java.util.ArrayList;

public class RegistrodePedidos{
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;

    public RegistrodePedidos(){
        listaPreparacion = new ArrayList<>();
        listaTransito = new ArrayList<>();
    }

    public void add(Pedido pedido){
        listaPreparacion.add(pedido);
    }


    public Pedido get(){
        return listaPreparacion.getLast();
    }
}
