/**
 * Clase RegistrodePedidos que gestiona listas compartidas de pedidos en preparación y en tránsito.
 * Utiliza sincronización con objetos independientes para proteger el acceso concurrente a cada lista.
 */
import java.util.ArrayList;

public class RegistrodePedidos {
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;

    private final Object lockPreparacion = new Object();
    private final Object lockTransito = new Object();

    /**
     * Constructor que inicializa las listas y los objetos de sincronización.
     */
    public RegistrodePedidos() {
        listaPreparacion = new ArrayList<>();
        listaTransito = new ArrayList<>();
    }

    /**
     * Agrega un pedido a la lista de preparación.
     * @param pedido el pedido que será agregado.
     */
    public void addListaPreparacion(Pedido pedido) {
        synchronized(lockPreparacion) {
            listaPreparacion.add(pedido);
            lockPreparacion.notifyAll();
        }
    }

    /**
     * Obtiene y remueve el último pedido de la lista de preparación.
     * Espera si la lista está vacía.
     * @return el pedido listo para ser despachado.
     */
    public Pedido getListaPreparacion() {
        synchronized(lockPreparacion) {
            while (listaPreparacion.isEmpty()) {
                try {
                    lockPreparacion.wait();
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
        synchronized(lockTransito) {
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
