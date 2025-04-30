import java.util.ArrayList;

public class RegistrodePedidos{
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;

    public RegistrodePedidos(){
        listaPreparacion = new ArrayList<>();
        listaTransito    = new ArrayList<>();
    }

    public void addListaPreparacion(Pedido pedido){
        listaPreparacion.add(pedido);
    }

    public Pedido getListaPreparacion(){
        if (listaPreparacion.size()>1) {
            Pedido ped = listaPreparacion.getLast();
            listaPreparacion.removeLast();
            return ped;
        } 
        return null;
    }

    public void addListaTransito(Pedido pedido){
        listaTransito.add(pedido);
    }

    public void print(){
        System.out.println(listaTransito.size());
    }
}
