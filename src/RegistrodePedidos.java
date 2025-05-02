import java.util.ArrayList;

public class RegistrodePedidos{
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;

    private final Object obj1;
    private final Object obj2;

    public RegistrodePedidos(){
        listaPreparacion = new ArrayList<>();
        listaTransito    = new ArrayList<>();

        this.obj1 = new Object();
        this.obj2 = new Object();
    }

    public void addListaPreparacion(Pedido pedido){
        synchronized(obj1){
            listaPreparacion.add(pedido);
            obj1.notifyAll();
        }
    }

    public Pedido getListaPreparacion(){
        synchronized(obj1){
            while (listaPreparacion.isEmpty()) {
                try {
                    obj1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return listaPreparacion.removeLast();        
        }
    }

    public void addListaTransito(Pedido pedido){
        synchronized(obj2){
            listaTransito.add(pedido);
        }   
    }

    public void print(){
        System.out.println(listaTransito.size());
    }

    public int sizeListaTransito(){
        return listaTransito.size();
    }
}
