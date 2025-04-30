public class Casillero {
        int Estado;
        int Contador;

        Casillero(){
            Estado   = 0;
            Contador = 0;
        }

        public boolean estaVacio(){
            return Estado == 0 ;
        }

        public void ocupar(){
            Estado++;
            Contador++;
        }

        public void desocupar(){
            Estado--;
        }
}
