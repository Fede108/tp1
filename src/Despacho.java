public class Despacho {
    private SistemaAlmacenamiento sistema;
    private RegistrodePedidos pedidos;

    public Despacho(SistemaAlmacenamiento sistema, RegistrodePedidos pedidos){
        this.sistema = sistema;
        this.pedidos = pedidos;
    }

    public void llenarRegistro(){
        sistema.desocuparCasillero(pedidos.get());
    }

}
