public class Main {
    public static void main(String[] args) {
	
		SistemaAlmacenamiento sistemaAlmacenamiento = new SistemaAlmacenamiento();
        RegistrodePedidos     registrodePedidos     = new RegistrodePedidos();
        Preparacion preparacion = new Preparacion(sistemaAlmacenamiento, registrodePedidos);
        Despacho    despacho    = new Despacho(sistemaAlmacenamiento, registrodePedidos);

        Thread hilo1 = new Thread(preparacion);
        Thread hilo2 = new Thread(despacho);
        hilo1.start();
        hilo2.start();
    
	}
}
