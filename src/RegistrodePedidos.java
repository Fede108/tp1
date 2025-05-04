/**
 * Clase RegistrodePedidos que gestiona listas compartidas de pedidos en preparación y en tránsito.
 * Utiliza sincronización con objetos independientes para proteger el acceso concurrente a cada lista.
 */
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RegistrodePedidos {
    private ArrayList<Pedido> listaPreparacion;
    private ArrayList<Pedido> listaTransito;
    private ArrayList<Pedido> listaEntregados;
    private ArrayList<Pedido> listaFallidos;

  //  private final Object lockPreparacion = new Object();
  //  private final Object lockTransito    = new Object();
  //  private final Object lockEntregados  = new Object();
  //  private final Object lockFallidos    = new Object(); 

    private ReentrantLock lockPrep = new ReentrantLock(true);
    private final Condition listaLibre = lockPrep.newCondition();

    private final ReentrantLock lockTrans   = new ReentrantLock(true);
    private final Condition  condTrans     = lockTrans.newCondition();

    private final ReentrantLock lockEntreg  = new ReentrantLock(true);
    private final Condition  condEntreg    = lockEntreg.newCondition();

    private final ReentrantLock lockFall    = new ReentrantLock(true);
    private final Condition  condFall      = lockFall.newCondition();

    /**
     * Constructor que inicializa las listas y los objetos de sincronización.
     */
    public RegistrodePedidos( ) {
        listaPreparacion = new ArrayList<>();
        listaTransito    = new ArrayList<>();
        listaEntregados  = new ArrayList<>();
        listaFallidos    = new ArrayList<>();
    }

    public void getLockListaPreparacion(){
        lockPrep.lock();
    }

    /**
     * Agrega un pedido a la lista de preparación.
     * @param pedido el pedido que será agregado.
     */
    public void addListaPreparacion(Pedido pedido) {
        try{
            listaPreparacion.add(pedido);
            listaLibre.signalAll();
        }finally{
            lockPrep.unlock();
        }
    }

    /**
     * Obtiene y remueve el último pedido de la lista de preparación.
     * Espera si la lista está vacía.
     * @return el pedido listo para ser despachado.
     */
    public Pedido getListaPreparacion() {
        lockPrep.lock();
        try{
            while (listaPreparacion.isEmpty()) {
                try {
                    listaLibre.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return listaPreparacion.removeLast();   
        }finally{
            lockPrep.unlock();
        }
    }

    /**
     * Agrega un pedido a la lista de tránsito.
     * @param pedido el pedido que ha sido despachado.
     */
    public void addListaTransito(Pedido pedido) {
        lockTrans.lock();
        try {
            listaTransito.add(pedido);
            condTrans.signalAll();
        } finally {
            lockTrans.unlock();
        }
    }

     /**
     * Obtiene y remueve el último pedido de la lista de Transito.
     * Espera si la lista está vacía.
     * @return el pedido listo para ser entregado.
     */
    public Pedido getListaTransito() {
        lockTrans.lock();
        try {
            while (listaTransito.isEmpty()) {
                condTrans.awaitUninterruptibly();
            }
            return listaTransito.remove(listaTransito.size() - 1);
        } finally {
            lockTrans.unlock();
        }
    }
    

     /**
     * Agrega un pedido a la lista de entrega.
     * @param pedido el pedido que ha sido entregado.
     */
    public void addListaEntregados(Pedido pedido) {
        lockEntreg.lock();
        try {
            listaEntregados.add(pedido);
            condEntreg.signalAll();
        } finally {
            lockEntreg.unlock();
        }
    }

    /**
     * Agrega un pedido a la lista de fallidos.
     */
    public Pedido getListaEntregados() {
        lockEntreg.lock();
        try {
            while (listaEntregados.isEmpty()) {
                condEntreg.awaitUninterruptibly();
            }
            return listaEntregados.remove(listaEntregados.size() - 1);
        } finally {
            lockEntreg.unlock();
        }
    }

     /**
     * Agrega un pedido a la lista de fallados.
     * @param pedido el pedido que ha fallado.
     */
    public void addListaFallidos(Pedido pedido) {
        lockFall.lock();
        try {
            listaFallidos.add(pedido);
            condFall.signalAll();
        } finally {
            lockFall.unlock();
        }
    }

    /**
     * Imprime la cantidad de pedidos en tránsito.
     */
    public void print() {
        System.out.printf("\nCantidad pedidos en transito  %d\n ",listaTransito.size());
        System.out.printf("\nCantidad pedidos en fallidos  %d\n ",listaFallidos.size());
        System.out.printf("\nCantidad pedidos en entregados  %d\n ",listaEntregados.size());
    }

    /**
     * Retorna el tamaño de la lista de pedidos en tránsito.
     * @return cantidad de pedidos despachados exitosamente.
     */
    public int sizeListaTransito() {
        return listaTransito.size();
    }
}
