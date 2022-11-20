package simulador.so;


public class Proceso implements Comparable<Proceso>{
    //Atributos
    int PID;
    int TA;
    int Tam;
    int Estado;     //0 = nuevo, 1 = Listo, 2= Listo/Susp, 3= En Ejecucion, 4= Finalizado
    int TI;

    
    Proceso (int PID, int TA, int Tam, int Estado, int TI){
        this.PID = PID;
        this.TA = TA;
        this.Tam = Tam;
        this.Estado = Estado;
        this.TI = TI;
    }
    
   public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public int getTA() {
        return TA;
    }

    public void setTA(int TA) {
        this.TA = TA;
    }

    public int getTam() {
        return Tam;
    }

    public void setTam(int Tam) {
        this.Tam = Tam;
    }
    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }
    public int getTI() {
        return TI;
    }

    public void setTI(int TI) {
        this.TI = TI;
    }
    
    @Override
        public int compareTo(Proceso p) {
            if (TA < p.TA) {
                return -1;
            }
            if (TA > p.TA) {
                return 1;
            }
            if (TA==p.TA){
            if (TI<p.TI){
                return -1;
            }
            if (TI>p.TI){
                return 1;
                }
            }
            return 0;
        }
        
}