package simulador.so;


public class CPU {

   //Atributos
    int PID;
    int NroPart;
    int TA;
    int Tam;
    int CPU;    
    boolean libre;   

    public CPU(int PID,int NroPart, int TA, int Tam, int CPU, boolean libre) {
        this.PID = PID;
        this.NroPart = NroPart;
        this.TA = TA;
        this.Tam = Tam;
        this.CPU = CPU;
        this.libre = libre;
    }
    
    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }
    public int getNroPart() {
        return NroPart;
    }

    public void setNroPart(int NroPart) {
        this.NroPart = NroPart;
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

    public int getCPU() {
        return CPU;
    }

    public void setCPU(int CPU) {
        this.CPU = CPU;
    }

    public boolean isLibre() {
        return libre;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }
}
