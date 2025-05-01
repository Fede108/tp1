import java.util.ArrayList;

public class RegistrodePedidos{
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;

    public RegistrodePedidos(){
        listaPreparacion = new ArrayList<>();
        listaTransito    = new ArrayList<>();
    }

    public synchronized void addListaPreparacion(Pedido pedido){
        listaPreparacion.add(pedido);
        notifyAll();
    }

    public synchronized Pedido getListaPreparacion(){
        while (listaPreparacion.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return listaPreparacion.removeLast();        
    }

    public synchronized void addListaTransito(Pedido pedido){
        listaTransito.add(pedido);
    }

    public void print(){
        System.out.println(listaTransito.size());
    }
}
