import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Modelo de sistema de almacenamiento concurrente mediante casilleros.
 * Un hilo productor ocupa casilleros y un hilo consumidor los desocupa.
 */
public class SistemaAlmacenamiento {
    private ArrayList<Casillero> matriz;
    private int casillerosVisitados = 0;
//    private HashMap<Integer, Casillero> casillerosVisitados;
    private Integer cantPedidos;
    private Integer totalPedidos;

    private static final int N_CASILLEROS = 200;

    // Objeto lock dedicado para las secciones críticas
   // private final Object lockCasillero = new Object();
    private ReentrantLock lockCasillero = new ReentrantLock(true);
    private final Condition casilleroLibre = lockCasillero.newCondition();
  

    /**
     * Constructor del sistema.
     * @param totalPedidos cantidad total de pedidos a gestionar en el sistema.
     */
    SistemaAlmacenamiento(Integer totalPedidos) {
        matriz = new ArrayList<>(N_CASILLEROS);
        for (int i = 0; i < N_CASILLEROS; i++) {
            matriz.add(new Casillero());
        }
        cantPedidos = 0;
        this.totalPedidos = totalPedidos;
    //    casillerosVisitados = new HashMap<>();
    }

    /**
     * Método para ocupar un casillero vacío y funcional.
     * Espera si todos los casilleros están llenos.
     * @return Pedido asociado al casillero ocupado.
     */
    public Pedido ocuparCasillero() {
        lockCasillero.lock();
        try {
            int nroCasillero;
            Casillero casillero;

            while (casillerosVisitados == N_CASILLEROS) {
                log("CASILLEROS_LLENOS", null);
                try {
                    casilleroLibre.await();    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                nroCasillero = new Random().nextInt(N_CASILLEROS);
                casillero = matriz.get(nroCasillero);
                if (casillero.estaVacio()) {
                  //  casillerosVisitados.put(nroCasillero, casillero);
                    casillerosVisitados++;
                    casillero.ocupar();
                    break;
                }
            }

            Pedido pedido = new Pedido(nroCasillero, ++cantPedidos);
            log("CASILLERO_OCUPADO ", pedido);
            return pedido;
        } catch (Exception e) {
            return null;
        } finally {
            lockCasillero.unlock();
        }
    }

    public void getlockCasillero(){
        lockCasillero.lock();
    }

    /**
     * Método para desocupar un casillero previamente ocupado.
     * Notifica a hilos en espera de casilleros disponibles.
     * @param pedido Pedido asociado al casillero a desocupar.
     */
    public void desocuparCasillero(Pedido pedido) {
        lockCasillero.lock();
        try {
            matriz.get(pedido.getCasillero()).desocupar();
            casillerosVisitados--;
        //    casillerosVisitados.remove(pedido.getCasillero());
            casilleroLibre.signalAll(); 
            log("CASILLERO_LIBERADO", pedido);
        } catch (Exception e) {
            // TODO: handle exception
        } finally{
            lockCasillero.unlock();
        }
    }

    /**
     * Marca un casillero como fuera de servicio.
     * @param pedido Pedido asociado al casillero fallido.
     */
    public void setCasilleroFueraServicio(Pedido pedido) {
            lockCasillero.lock();
            try {
                matriz.get(pedido.getCasillero()).setFueraServicio();
                log("PEDIDO_FALLIDO   ", pedido);
          //      System.err.println(pedido.getCasillero());
           //     casillerosVisitados.remove(pedido.getCasillero());    
            } catch (Exception e) {
                // TODO: handle exception
            } finally{
                lockCasillero.unlock();
            }
    }

    /**
     * @return Total de pedidos que se deben procesar.
     */
    public Integer getTotalPedidos() {
        return totalPedidos;
    }

    /**
     * @return Cantidad de casilleros marcados como fuera de servicio.
     */
    public Integer getCasillerosFallidos() {
        int fallidos = 0;
        for (int i = 0; i < N_CASILLEROS; i++) {
            if (matriz.get(i).estaFueraServicio()) {
                fallidos++;
            }
        }
        return fallidos;
    }

    /**
     * @return Cantidad de casilleros funcionales.
     */
    public Integer getCasillerosFuncionales() {
        int funcionando = 0;
        for (int i = 0; i < N_CASILLEROS; i++) {
            if (!matriz.get(i).estaFueraServicio()) {
                funcionando++;
            }
        }
        return funcionando;
    }

    /**
     * Registra en consola información sobre acciones realizadas en el sistema.
     * @param accion Descripción de la acción.
     * @param pedido Pedido involucrado (puede ser null).
     */
    private void log(String accion, Pedido pedido) {
        String msg = String.format("%1$tF %1$tT.%1$tL [%2$s] %3$s %4$s",
                new Date(),
                Thread.currentThread().getName(),
                accion,
                (pedido != null ? pedido : ""));
        System.out.println(msg);
    }
}
