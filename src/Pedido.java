public class Pedido {
    private Integer casillero;
    public int id;

    Pedido(Integer casillero, int id){
        this.casillero = casillero;
        this.id = id;
    }

    public Integer getCasillero(){
        return casillero;
    }
}
