public class Main {
    public static void main(String[] args) {
	
		SistemaAlmacenamiento sistemaAlmacenamiento = new SistemaAlmacenamiento(20);
    RegistrodePedidos     registrodePedidos     = new RegistrodePedidos();
    Preparacion preparacion                     = new Preparacion(sistemaAlmacenamiento, registrodePedidos);
    Despacho    despacho                        = new Despacho   (sistemaAlmacenamiento, registrodePedidos);
      
    Thread[] hilos = {
      new Thread(preparacion),
      new Thread(despacho),
      new Thread(preparacion),
      new Thread(despacho),
      new Thread(preparacion),
      new Thread(despacho)
    };

    for (Thread t : hilos) {
      t.start();
    }

    for (Thread t : hilos) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    despacho.print();
    
	}
}
