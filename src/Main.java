import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
    long startTime = System.currentTimeMillis();

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
    };

    for (Thread t : hilos) {
      t.start();
    }

    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter("registro.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    boolean algunoVivo = true;

    while (algunoVivo) {
      for (Thread thread : hilos) {
        algunoVivo = false;
        if (thread.isAlive()) 
          {
            algunoVivo = true;
          }
      }
      
      Registro(startTime, sistemaAlmacenamiento, registrodePedidos, writer, false);
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    

    for (Thread t : hilos) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    Registro(startTime, sistemaAlmacenamiento, registrodePedidos, writer, true);
    despacho.print();
    
	}

  public static void Registro(long startTime, SistemaAlmacenamiento sistema, RegistrodePedidos registro, BufferedWriter writer, boolean lineaFinal) {
    long programTime = System.currentTimeMillis() - startTime;
    String prefix = "[" + programTime + " ms] ";  

    
    try {
        String linea = prefix + " Pedidos despachados: " + registro.sizeListaTransito();
        writer.write(linea);
        writer.newLine();
        writer.flush();
      } catch (IOException e){
        e.printStackTrace();
      }
      if (lineaFinal) {
        try {
          String linea = prefix + " Casilleros fallidos: " + sistema.getCasillerosFallidos();
          writer.write(linea);
          writer.newLine();
          writer.flush();
        } catch (IOException e){
          e.printStackTrace();
        }
      }
  }
}
