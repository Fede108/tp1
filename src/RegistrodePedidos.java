/**
 * Clase RegistrodePedidos que gestiona listas compartidas de pedidos en preparación y en tránsito.
 * Utiliza sincronización con objetos independientes para proteger el acceso concurrente a cada lista.
 */
import java.util.ArrayList;

public class RegistrodePedidos {
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;

    private final Object obj1;
    private final Object obj2;

    /**
     * Constructor que inicializa las listas y los objetos de sincronización.
     */
    public RegistrodePedidos() {
        listaPreparacion = new ArrayList<>();
        listaTransito = new ArrayList<>();

        this.obj1 = new Object();
        this.obj2 = new Object();
    }

    /**
     * Agrega un pedido a la lista de preparación.
     * @param pedido el pedido que será agregado.
     */
    public void addListaPreparacion(Pedido pedido) {
        synchronized(obj1) {
            listaPreparacion.add(pedido);
            obj1.notifyAll();
        }
    }

    /**
     * Obtiene y remueve el último pedido de la lista de preparación.
     * Espera si la lista está vacía.
     * @return el pedido listo para ser despachado.
     */
    public Pedido getListaPreparacion() {
        synchronized(obj1) {
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

    /**
     * Agrega un pedido a la lista de tránsito.
     * @param pedido el pedido que ha sido despachado.
     */
    public void addListaTransito(Pedido pedido) {
        synchronized(obj2) {
            listaTransito.add(pedido);
        }   
    }

    /**
     * Imprime la cantidad de pedidos en tránsito.
     */
    public void print() {
        System.out.println(listaTransito.size());
    }

    /**
     * Retorna el tamaño de la lista de pedidos en tránsito.
     * @return cantidad de pedidos despachados exitosamente.
     */
    public int sizeListaTransito() {
        return listaTransito.size();
    }
}
