public class Main {
    public static void main(String[] args) {
	
		SistemaAlmacenamiento sistemaAlmacenamiento = new SistemaAlmacenamiento(500);
    RegistrodePedidos     registrodePedidos     = new RegistrodePedidos();
    Preparacion preparacion = new Preparacion(sistemaAlmacenamiento, registrodePedidos);
    Despacho    despacho    = new Despacho(sistemaAlmacenamiento, registrodePedidos);
      
    Thread hilo1 = new Thread(preparacion);
    Thread hilo2 = new Thread(despacho);

    Thread hilo3 = new Thread(preparacion);
    Thread hilo4 = new Thread(despacho);

    Thread hilo5 = new Thread(preparacion);
    Thread hilo6 = new Thread(despacho);

    hilo1.start();
    hilo2.start();
    hilo3.start();
    hilo4.start();
    hilo5.start();
    hilo6.start();
    
	}
}
