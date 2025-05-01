public class Pedido {
    private Integer casillero;
    private int Estado;
    public int id;

    Pedido(Integer casillero, int id){
        this.casillero = casillero;
        this.id = id;
    }

    public Integer getCasillero(){
        return casillero;
    }

    public void setFallido(){
        Estado = -1;
    }

    public String toString() {
        return "Pedido #" + id;
    }
}
