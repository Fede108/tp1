import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Modelo de sistema de almacenamiento concurrente mediante casilleros.
 * Un hilo productor ocupa casilleros y un hilo consumidor los desocupa.
 */
public class SistemaAlmacenamiento {
    private ArrayList<Casillero> matriz;
    private HashMap<Integer, Casillero> casillerosVisitados;
    private Integer cantPedidos;
    private Integer totalPedidos;

    private static final int N_CASILLEROS = 50; 

    /**
     * Constructor del sistema.
     * @param totalPedidos cantidad total de pedidos a gestionar en el sistema.
     */
    SistemaAlmacenamiento(Integer totalPedidos)
    {
        matriz = new ArrayList<>(N_CASILLEROS);
        for (int i = 0; i < N_CASILLEROS; i++) {
            matriz.add(new Casillero());
        }
        cantPedidos = 0;
        this.totalPedidos   = totalPedidos;
        casillerosVisitados = new HashMap<>();
    }

    /**
     * Método sincronizado para ocupar un casillero vacío y funcional.
     * Espera si todos los casilleros están llenos.
     * @return Pedido asociado al casillero ocupado.
     */
    public synchronized Pedido ocuparCasillero() 
    {
        int nroCasillero;
        Casillero casillero;

        while (casillerosVisitados.size() == N_CASILLEROS) 
        {
            log("CASILLEROS_LLENOS", null);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            nroCasillero = new Random().nextInt(N_CASILLEROS);
            casillero    = matriz.get(nroCasillero);
            if (!casillerosVisitados.containsKey(nroCasillero) && casillero.estaVacio()) {
                casillerosVisitados.put(nroCasillero, casillero);
                casillero.ocupar();
                break;
            }
        }

        Pedido pedido = new Pedido(nroCasillero, ++cantPedidos);
        log("OCUPAR_CASILLERO  ", pedido);
        return pedido;
    }

    /**
     * Método sincronizado para desocupar un casillero previamente ocupado.
     * Notifica a hilos en espera de casilleros disponibles.
     * @param pedido Pedido asociado al casillero a desocupar.
     */
    public synchronized void desocuparCasillero(Pedido pedido){
        matriz.get(pedido.getCasillero()).desocupar();
        casillerosVisitados.remove(pedido.getCasillero());
        notify();
        log("CASILLERO_LIBERADO", pedido);
    }

    /**
     * Marca un casillero como fuera de servicio.
     * @param pedido Pedido asociado al casillero fallido.
     */
    public synchronized void setCasilleroFueraServicio(Pedido pedido)
    {
         matriz.get(pedido.getCasillero()).setFueraServicio();
         log("PEDIDO_FALLIDO", pedido);
    }

    /**
     * @return Total de pedidos que se deben procesar.
     */
    public Integer getTotalPedidos(){
        return totalPedidos;
    }

    /**
     * @return Cantidad de casilleros marcados como fuera de servicio.
     */
    public Integer getCasillerosFallidos(){
        Integer fallidos = 0;
        for (int i = 0; i < N_CASILLEROS; i++) {
            if(matriz.get(i).estaFueraServicio()){
                fallidos++;
            }
        }
        return fallidos;
    }

    /**
     * @return Cantidad de casilleros funcionales.
     */
    public Integer getCasillerosFuncionales(){
        Integer funcionando = 0;
        for (int i = 0; i < N_CASILLEROS; i++) {
            if(!matriz.get(i).estaFueraServicio()){
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
