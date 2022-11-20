package simulador.so;

public class Particion {
   int NroPart;
   int ProCargado;
   int TamPart;
   boolean libre;
   int FragInt;
   int DirCom; //en java todos los punteros son explícitos así que no puedo usarlos convencionalmente
   

    public Particion(int NroPart, int ProCargado, int TamPart, boolean libre, int FragInt, int DirCom) {
        this.NroPart = NroPart;
        this.ProCargado = ProCargado;
        this.TamPart = TamPart;
        this.libre = libre;
        this.FragInt = FragInt;
        this.DirCom = DirCom;
    }
    
    public int getProCargado() {
        return ProCargado;
    }

    public void setProCargado(int ProCargado) {
        this.ProCargado = ProCargado;
    }
   
    public int getNroPart() {
        return NroPart;
    }

    public void setNroPart(int NroPart) {
        this.NroPart = NroPart;
    }

    public int getTamPart() {
        return TamPart;
    }

    public void setTamPart(int TamPart) {
        this.TamPart = TamPart;
    }

    public boolean isLibre() {
        return libre;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }
   public int getDirCom() {
        return DirCom;
    }
   public void setDirCom(int DirCom) {
        this.DirCom = DirCom;
    }
   public int getFragInt() {
        return FragInt;
    }
   public void setFragInt(int FragInt) {
        this.FragInt = FragInt;
    }
   
}
