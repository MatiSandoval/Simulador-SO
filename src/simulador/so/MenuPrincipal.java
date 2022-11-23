package simulador.so;

import simulador.so.Particion;
import simulador.so.CPU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileWriter;
import static java.awt.Frame.NORMAL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
public class MenuPrincipal extends javax.swing.JFrame {

    public File carpetaSeleccionada;
    UIManager UI;
    Integer x=0,inst= -1,multip=0,mascorto=100,p_mascorto=0,tiempoproceso =0,tpp=0,xo=0;
    Integer Fr1=0,Fr2=0,Fr3=0,f1=0,f2=0,f3=0;
    Boolean cargop=false,bul=false;
    Proceso proceso1,proceso2,proceso3,procesocpu;
    Particion particiones[];
    CPU cpu[];
    ArrayList<CPU> CPU;
    ArrayList<Particion> memoriaFija;
    ArrayList<Proceso> lista = new ArrayList<Proceso>();      //Lista completa de la clase Proceso
    
    ArrayList<Proceso> colaListo = new ArrayList<Proceso>();    //Lista de procesos que esperam por la CPU
    ArrayList<Proceso> colaVivos = new ArrayList<Proceso>();    //Lista con los procesos que esperan por ingresar a la cola de listo y a memoria o disco
    ArrayList<Proceso> colaNuevo = new ArrayList<Proceso>();    //Lista de procesos admitidos pero no da la multiprogramación
    ArrayList<Proceso> colaListoSuspendido = new ArrayList<Proceso>();    //Lista que contiene los procesos que estan en disco
    ArrayList<Proceso> colaTerminados = new ArrayList<Proceso>();    //Lista que contiene los procesos que terminaron su ejecucion
    
    public MenuPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        UI.put("OptionPane.messageForeground", Color.black);
        crearMemoriaFija();
        UIManager.put( "nimbusOrange", new Color( 38, 139, 210 ) );
        
        CargarExcel();
        Carga();
        crearMemoriaFija();
        CargarProcesos();
        
        
        p.addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if (colaListo.isEmpty() && colaListoSuspendido.isEmpty() && colaNuevo.isEmpty() && colaVivos.isEmpty()){JOptionPane.showOptionDialog(null, "Simulación terminada.", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);}
            else{if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                inst=inst+1;
                
                p.setText("Instante "+inst);
                ejecutar3();ejecutar();ejecutar2();
                
                if(tiempoproceso>0){
                    if(cpu[0].NroPart==0){pb1.setVisible(true);pb2.setVisible(false);pb3.setVisible(false);}
                    if(cpu[0].NroPart==1){pb1.setVisible(false);pb2.setVisible(true);pb3.setVisible(false);}
                    if(cpu[0].NroPart==2){pb1.setVisible(false);pb2.setVisible(false);pb3.setVisible(true);}
                    tiempoproceso-=1;}
                
            }}
            }
    });
        
    }
    
    
    public static void crearExcel(){
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("PROCESOS");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID PROCESO");
        row.createCell(1).setCellValue("TAMAÑO");
        row.createCell(2).setCellValue("TA");
        row.createCell(3).setCellValue("TI");
        //Cell celda2 = rowUno.createCell(2);
        //celda2.setCellFormula(String.format("A%d+B%d", 2,2));
        //Cell celda = row.createCell(5);
        //celda.setCellFormula(String.format("1+1", ""));
        //Proceso 1
        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(1);
        row1.createCell(1).setCellValue(200);
        row1.createCell(2).setCellValue(0);
        row1.createCell(3).setCellValue(5);
        //Proceso 2
        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue(2);
        row2.createCell(1).setCellValue(100);
        row2.createCell(2).setCellValue(1);
        row2.createCell(3).setCellValue(3);
        //Proceso 3
        Row row3 = sheet.createRow(3);
        row3.createCell(0).setCellValue(3);
        row3.createCell(1).setCellValue(60);
        row3.createCell(2).setCellValue(2);
        row3.createCell(3).setCellValue(2);
        //Proceso 4
        Row row4 = sheet.createRow(4);
        row4.createCell(0).setCellValue(4);
        row4.createCell(1).setCellValue(200);
        row4.createCell(2).setCellValue(3);
        row4.createCell(3).setCellValue(3);
        //Proceso 5
        Row row5 = sheet.createRow(5);
        row5.createCell(0).setCellValue(5);
        row5.createCell(1).setCellValue(100);
        row5.createCell(2).setCellValue(4);
        row5.createCell(3).setCellValue(4);
        //Proceso 6
        Row row6 = sheet.createRow(6);
        row6.createCell(0).setCellValue(6);
        row6.createCell(1).setCellValue(60);
        row6.createCell(2).setCellValue(5);
        row6.createCell(3).setCellValue(3);
        //Proceso 7
        Row row7 = sheet.createRow(7);
        row7.createCell(0).setCellValue(7);
        row7.createCell(1).setCellValue(200);
        row7.createCell(2).setCellValue(6);
        row7.createCell(3).setCellValue(2);
        //Proceso 8
        Row row8 = sheet.createRow(8);
        row8.createCell(0).setCellValue(8);
        row8.createCell(1).setCellValue(100);
        row8.createCell(2).setCellValue(7);
        row8.createCell(3).setCellValue(4);
        //Proceso 9
        Row row9 = sheet.createRow(9);
        row9.createCell(0).setCellValue(9);
        row9.createCell(1).setCellValue(60);
        row9.createCell(2).setCellValue(8);
        row9.createCell(3).setCellValue(4);
        //Proceso 10
        Row row10 = sheet.createRow(10);
        row10.createCell(0).setCellValue(10);
        row10.createCell(1).setCellValue(60);
        row10.createCell(2).setCellValue(8);
        row10.createCell(3).setCellValue(3);
        
        try {
            FileOutputStream fileout = new FileOutputStream("Procesos.xlsx");
            book.write(fileout);
            fileout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    
    public void leer(){
        boolean isRowEmpty=true;
        try {
            String[] filas = new String[4];
            DefaultTableModel modelo = (DefaultTableModel)tabla.getModel();
            FileInputStream file = new FileInputStream (new File(".\\Procesos.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(0);
            int numFilas = sheet.getLastRowNum();
            for(int i = 0; i < sheet.getLastRowNum(); i++){
            if(sheet.getRow(i)==null){
            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
            i--;
            continue;
            }
            for(int j =0; j<sheet.getRow(i).getLastCellNum();j++){
            if(sheet.getRow(i).getCell(j).toString().trim().equals("")){
                isRowEmpty=true;
            }else {
                isRowEmpty=false;
                break;
            }
            }
            if(isRowEmpty==true){
            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
            i--;
            numFilas=i;
            }}
            
            //numFilas=numFilas-1;
            if (numFilas>10){JOptionPane.showOptionDialog(null, "No puede agregar más de 10 procesos", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);}
            if (numFilas <=10){
            for(int a=0; a <=numFilas; a++){
                if(a==0){filas[0]="0";filas[1]="Sistema Operativo";filas[2]="-";filas[3]="-";}
                Row fila = sheet.getRow(a);
                int numCols = fila.getLastCellNum();
                for(int b=0;b< numCols; b++){
                    Cell celda = fila.getCell(b);
                    if (celda!=null){
                    switch(celda.getCellType().toString()){
                        case "NUMERIC":
                            double x = celda.getNumericCellValue();
                            int y = (int) Math.round(x);
                            
                            filas[b]=""+y;
                            
                            break;
                        case "STRING":
                            
                            break;
                        case "FORMULA":
                            
                            break;
                    }}
                    
                }
                
                modelo.addRow((Object[])filas);
            }}
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            crearExcel();bul=true;
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
    public void crearMemoriaFija(){
        particiones = new Particion[3];
            particiones[0] = new Particion(0, 0, 60, true, 0 , 101); 
            particiones[1] = new Particion(1, 0, 120, true, 0 , 161);
            particiones[2] = new Particion(2, 0, 250, true, 0 , 281);
        cpu = new CPU[1];
        cpu[0] = new CPU (0,-1,0,0,0,true);
        pb1.setVisible(false);
        pb2.setVisible(false);
        pb3.setVisible(false);
        
    }
    
    public void modificar(){
        int maxfila = tabla.getRowCount();
        String id,tam,ta,ti;
        int b=0;
        DefaultTableModel modelo = (DefaultTableModel)tabla.getModel();
        modelo.setValueAt("Sistema Operativo",0,1);
        modelo.setValueAt("-",0,2);
        modelo.setValueAt("-",0,3);
        for (int j =1; j<maxfila;j++){
        id=tabla.getValueAt(j, 0).toString();
        tam=tabla.getValueAt(j, 1).toString();
        ta=tabla.getValueAt(j, 2).toString();
        ti=tabla.getValueAt(j, 3).toString();
        
        try {
            FileInputStream file = new FileInputStream (new File(".\\Procesos.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(0);
            int id1 = Integer.parseInt(id);
            String o = ""+sheet.getRow(id1).getCell(1).toString();
            String p = ""+sheet.getRow(id1).getCell(3).toString();
            XSSFRow fila = sheet.getRow(id1);
            if(fila == null){
                fila = sheet.createRow(id1);}
            XSSFCell celda = fila.createCell(1);
            XSSFCell celda2 = fila.createCell(2);
            XSSFCell celda3 = fila.createCell(3);
            if (celda == null){
                celda = fila.createCell(1);}
            if (celda2 == null){
                celda2 = fila.createCell(2);}
            if (celda3 == null){
                celda3 = fila.createCell(3);}
            int tam1 = Integer.parseInt(tam);
            double x = Double.parseDouble(o);
            double z = Double.parseDouble(p);
            int y = (int) Math.round(x);
            int w = (int) Math.round(z);
            int ta1 = Integer.parseInt(ta);
            int ti1 = Integer.parseInt(ti);
            
            if (ti1<=0){b=b+1;JOptionPane.showOptionDialog(null, "No puede agregar procesos con tiempo de irrupción iguales o menores a cero.", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);
            celda.setCellValue(y);
            celda2.setCellValue(ta1);
            celda3.setCellValue(w);
            modelo.setValueAt(y, id1, 1);
            modelo.setValueAt(ta1, id1, 2);
            modelo.setValueAt(w, id1, 3);
            
            if (tam1<=0 || tam1>250){b=b+1;JOptionPane.showOptionDialog(null, "No puede agregar procesos con tamaño =< 0KB o que superen los 250KB.", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);
            celda.setCellValue(y);
            celda2.setCellValue(ta1);
            celda3.setCellValue(w);
            modelo.setValueAt(y, id1, 1);
            modelo.setValueAt(ta1, id1, 2);
            modelo.setValueAt(w, id1, 3);}
            
            
            if (tam1<=250 && tam1>0){
            modelo.setValueAt(tam1, id1, 1);
            modelo.setValueAt(ta1, id1, 2);
            modelo.setValueAt(w, id1, 3);
            celda.setCellValue(tam1);
            celda2.setCellValue(ta1);
            celda3.setCellValue(w);
            }
            
            }else{
            if (tam1<=0 || tam1>250){b=b+1;JOptionPane.showOptionDialog(null, "No puede agregar procesos con tamaño =< 0KB o que superen los 250KB.", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);
            celda.setCellValue(y);
            celda2.setCellValue(ta1);
            celda3.setCellValue(ti1);
            modelo.setValueAt(y, id1, 1);
            modelo.setValueAt(ta1, id1, 2);
            modelo.setValueAt(ti1, id1, 3);}
            
            
            if (tam1<=250 && tam1>0){
            modelo.setValueAt(tam1, id1, 1);
            modelo.setValueAt(ta1, id1, 2);
            modelo.setValueAt(ti1, id1, 3);
            celda.setCellValue(tam1);
            celda2.setCellValue(ta1);
            celda3.setCellValue(ti1);
            }
            
            }
            
            
            
            file.close();
            FileOutputStream output = new FileOutputStream(".\\Procesos.xlsx");
            wb.write(output);
            output.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }}
        if (b==0){JOptionPane.showOptionDialog(null, "Datos guardados satisfactoriamente.", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);}
        
  }
    public void CargarExcel(){
       JFileChooser selectorCarpeta = new JFileChooser();
       selectorCarpeta.setCurrentDirectory(new File("."));
       selectorCarpeta.setDialogTitle("Seleccione la carpeta para guardar los procesos");
       selectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       selectorCarpeta.setAcceptAllFileFilterUsed(false);
       if (selectorCarpeta.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
           carpetaSeleccionada = selectorCarpeta.getCurrentDirectory();
           
       }else{System.exit(0);}
     
  }
    private void CargarProcesos(){
        cn.setText("Cola de Nuevos: ");
        if(!lista.isEmpty()){lista.clear();} //si se cargó anteriormente (al modificar un dato o volver a empezar)
        int ta=0,tam=0,ti=0;String k="";
        for (int i=1;i<tabla.getRowCount();i++){
                k=tabla.getValueAt(i, 1).toString();
                tam=Integer.parseInt(k);
                k=tabla.getValueAt(i, 2).toString();
                ta=Integer.parseInt(k);
                k=tabla.getValueAt(i, 3).toString();
                ti=Integer.parseInt(k);     
        
        proceso1 = new Proceso(i, ta,tam,0,ti);
        lista.add(proceso1);
        }
    Collections.sort(lista); //Ordena la lista por tiempo de arribo, y si son iguales por tiempo de irrupcion
    
    colaVivos = lista;
    
    
    String o="       ";
    for (int i=0;i<colaVivos.size();i++){
    o += "       | "+colaVivos.get(i).PID+" |";}
    cn.setText("Procesos sin arribar: "+o);
}
    
    
    public void ejecutar(){
    int indicee=0,instante = inst;
    boolean cargoo = false,b1=false,b2=false,b3=false,cnuevos=false;
    
    while (indicee< colaVivos.size()){ 
        cargoo=false;
        if(!colaListo.isEmpty()){
       multip=colaListo.size()+colaListoSuspendido.size();
        }
    if (colaVivos.get(indicee).TA <= instante) { //busco  que el tiempo de arribo del proceso esté en el instante
                                                   //plan. largo plazo
        
        if (multip < 5){
        cargoo = cargaWorstFit(colaVivos.get(indicee));
        
                
    }
    
    }
    
    
    if (cargoo==true){
                colaListo.add(colaVivos.get(indicee));
                colaVivos.remove(indicee);
                indicee--;        //Si cargo un proceso se resta, porque como se elimino un elemento de la colaNuevo, entonces el siguiente elemento va a ocupar ese lugar,
                
                
                }
    if(cargoo==false){
    if (colaVivos.get(indicee).TA <= instante) {
    if (multip < 5){
    colaListoSuspendido.add(colaVivos.get(indicee));
    colaVivos.remove(indicee);
    indicee--;        //Si cargo un proceso se resta, porque como se elimino un elemento de la colaNuevo, entonces el siguiente elemento va a ocupar ese lugar,
    
    }
    if(multip>=5){
    colaNuevo.add(colaVivos.get(indicee));
    colaVivos.remove(indicee);
    indicee--;}
    }
    }
    //System.out.println("Indice:"+indicee);
    indicee++;
    }
        
    
    }
    
    public void ejecutar2(){
    Boolean b1=false,b2=false,b3=false;    
    Integer f1=0,f2=0,f3=0,a1=0,a2=0,a3=0;        
    //if(tiempoproceso==0){
                      
    if(!cpu[0].libre && tiempoproceso==0){
    JOptionPane.showOptionDialog(null, "Terminó el proceso | P"+cpu[0].PID+" |", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 80, 80), null, NORMAL);
    TerminarProceso(cpu[0].PID);
    
    String t="       ";
                        if (!colaTerminados.isEmpty()){
                        for (int l=0;l<colaTerminados.size();l++){
                        t += "       | "+colaTerminados.get(l).PID+" |";
                        }}
                        cf.setText("Cola de Finalizados: "+t);
    //}
    pb1.setVisible(false);
    pb2.setVisible(false);
    pb3.setVisible(false);
    xo=0;
    }
    
    if(!colaNuevo.isEmpty()){
    if(multip<5){
    colaListoSuspendido.add(colaNuevo.get(0));   //si bajo la multiprogramacion un proceso nuevo puede entrar a disco 
    colaNuevo.remove(0);}
    }
    
     cargarCPU();
     pb1.setMaximum(tpp);
                        pb2.setMaximum(tpp);
                        pb3.setMaximum(tpp);
                        if (xo<tpp){
                        pb1.setValue(xo);
                        pb2.setValue(xo);
                        pb3.setValue(xo);
                        xo=xo+1;
                        }
    for (int i=0;i<3;i++){
    if(!particiones[i].libre){
    for(Proceso lista2 : colaListo){
                //busco el proceso mas corto por planificacion SJF, corto plazo
                if (lista2.PID==particiones[0].ProCargado){
                a1=lista2.Tam;
                }
                if (lista2.PID==particiones[1].ProCargado){
                a2=lista2.Tam;
                }
                if (lista2.PID==particiones[2].ProCargado){
                a3=lista2.Tam;
                }
                }
    }}
    f1=particiones[0].TamPart-a1;
    f2=particiones[1].TamPart-a2;
    f3=particiones[2].TamPart-a3;
    if (cpu[0].NroPart == 0){p1.setText("P"+particiones[0].ProCargado+" en ejecucion"+" FI: "+f1);}
                else{if(particiones[0].ProCargado!=0){p1.setText("P"+particiones[0].ProCargado+" "+"FI: "+f1);}}
    if (cpu[0].NroPart == 1){p2.setText("P"+particiones[1].ProCargado+ " en ejecucion"+" FI: "+f2);}else{
                if(particiones[1].ProCargado!=0){p2.setText("P"+particiones[1].ProCargado+" "+" FI: "+f2);}}
    if (cpu[0].NroPart == 2){p3.setText("P"+particiones[2].ProCargado+ " en ejecucion"+" FI "+f3);}
                else{if(particiones[2].ProCargado!=0){p3.setText("P"+particiones[2].ProCargado+" "+" FI: "+f3);}}
    String o="       ",m="       ",n="       ",ñ="       ";
                if(!colaVivos.isEmpty()){
                for (int i=0;i<colaVivos.size();i++){
                o += "       | "+colaVivos.get(i).PID+" |";
                }}
                
                if (!colaListo.isEmpty()){
                for (int j=0;j<colaListo.size();j++){
                m += "       | "+colaListo.get(j).PID+" |";
                }
                
                }
                
                if (!colaListoSuspendido.isEmpty()){
                for (int k=0;k<colaListoSuspendido.size();k++){
                n += "       | "+colaListoSuspendido.get(k).PID+" |";}}
                if (!colaNuevo.isEmpty()){
                for (int k=0;k<colaNuevo.size();k++){
                ñ += "       | "+colaNuevo.get(k).PID+" |";}}
                cn.setText("Procesos sin arribar: "+o);
                cl.setText("Cola de Listos: "+m);
                cls.setText("Cola de Listos/Suspendidos: "+n);
                cn1.setText("Cola de Nuevos: "+ñ);
                
    
        
    //    System.out.println("Part 1 "+particiones[0].ProCargado);
    //    System.out.println("Part 2: "+particiones[1].ProCargado);
    //    System.out.println("Part 3:"+particiones[2].ProCargado);
    //    System.out.println("Multip: "+multip);
    //System.out.println("Listos: "+colaListo.size());
    //for (int p=0;p<colaListo.size();p++){System.out.println("Listo: "+p+" "+colaListo.get(p).PID);}
    //System.out.println("Suspendidos: "+colaListoSuspendido.size());
    if(particiones[0].ProCargado==0){p1.setText("ESPACIO LIBRE");}
    if(particiones[1].ProCargado==0){p2.setText("ESPACIO LIBRE");}
    if(particiones[2].ProCargado==0){p3.setText("ESPACIO LIBRE");}
    }
    
    public void ejecutar3(){
    int indicee=0,instante = inst;
    boolean cargoo = false,b1=false,b2=false,b3=false,cnuevos=false;
    if (!colaNuevo.isEmpty()){
    while (indicee< colaNuevo.size()){ 
        cargoo=false;
        if(!colaListo.isEmpty()){
       multip=colaListo.size()+colaListoSuspendido.size();
        }
    
        
        if (multip < 5){
        cargoo = cargaWorstFit(colaNuevo.get(indicee));
        
                
    }
    
    
    
    
    
    
    if (cargoo==true){
                colaListo.add(colaNuevo.get(indicee));
                colaNuevo.remove(indicee);
                indicee--;        //Si cargo un proceso se resta, porque como se elimino un elemento de la colaNuevo, entonces el siguiente elemento va a ocupar ese lugar,
                
                
                }
    if(cargoo==false){
    
    if (multip < 5){
    colaListoSuspendido.add(colaNuevo.get(indicee));
    colaNuevo.remove(indicee);
    indicee--;        //Si cargo un proceso se resta, porque como se elimino un elemento de la colaNuevo, entonces el siguiente elemento va a ocupar ese lugar,
    
    }
    
    
    }
    //System.out.println("Indice:"+indicee);
    indicee++;
    }
        
    }
    }
    
    
    public boolean cargaWorstFit(Proceso proceso) {
        boolean cargo = false;
        int mayor = 0;
        int pos = 0;
        boolean existe = false;
        for (int i = 0; i < particiones.length; i++) { //Recorro las particiones hasta encontrar la primera que pueda ser asignada
            if ((particiones[i].libre) &(particiones[i].TamPart >= proceso.Tam)){
                mayor = particiones[i].TamPart;     //La guardo para comparar si hay otras mayores
                pos = i;    //Guardo la posicion, porque de no haber otra particion mayor, sera asignada a esta
                existe = true;  //Afirma que existe por lo menos una particion disponible para ser asignada
                break;
            }
        }
        for (int i = 0; i < particiones.length; i++){
            if ((particiones[i].libre) & (particiones[i].TamPart >= proceso.Tam)) {
                if (particiones[i].TamPart > mayor) {   //Busco si hay otra particion mayor disponible que pueda ser asignada
                    mayor = particiones[i].TamPart;
                    pos = i;
                }
            }
        }
        if (existe) {
            particiones[pos].ProCargado = proceso.PID;
            particiones[pos].libre = false;
            cargo = true;
        }
        return cargo;
    }
    public void PlanCortoPlazo(){
                boolean swp=false,libre=false,libero=false,b=false,b2=false,cargoo=false;
                mascorto=1000;
                Integer pos1=100,pos2=0,j=0,mayor=0,part=0,mayor2=0,pos3=0,y=0,part2=0;
                for(Proceso lista2 : colaListo){
                //busco el proceso mas corto por planificacion SJF, corto plazo
                if (lista2.TI<mascorto){
                mascorto=lista2.TI;
                p_mascorto=lista2.PID;
                }}
                for(Proceso lista3 : colaListoSuspendido){
                   for (int i=0;i<3;i++){          //recorro las particiones 
                    //if (cpu[0].NroPart!=particiones[i].NroPart){
                    if(lista3.Tam<particiones[i].TamPart && lista3.TI<mascorto){
                    if(particiones[i].libre && particiones[i].TamPart > mayor){
                    libre=true;mayor=particiones[i].TamPart;
                    mascorto=lista3.TI;
                    p_mascorto=lista3.PID;
                    swp=true;part=i;
                    pos1=j;
                    }
                    if(!particiones[i].libre && mayor2 > particiones[i].TamPart){
                    mascorto=lista3.TI;mayor2=particiones[i].TamPart;
                    p_mascorto=lista3.PID;
                    swp=true;part2=i;
                    pos1=j;
                    }
                }
                //}
                }
                j=j+1;
                }
                if (swp==true){     //swap in/out
                
                            if(libre == true){
                            
                            particiones[part].ProCargado = p_mascorto;
                            particiones[part].libre = false;
                            int d=pos1;
                            colaListo.add(colaListoSuspendido.get(d)); //agrego el proceso de la cola de L/S a la de listos
                            colaListoSuspendido.remove(d);
                            }else{
                            for(Proceso lista2 : colaListo){
                            //busco el proceso mas corto por planificacion SJF, corto plazo
                            if (particiones[part2].ProCargado == lista2.PID){
                            pos3=y;
                            }
                            y=y+1;
                            }
                            colaListo.add(colaListoSuspendido.get(j));
                            int l=j;int ñ=pos3;
                            colaListoSuspendido.remove(l);
                            colaListoSuspendido.add(colaListo.get(pos3));
                            colaListo.remove(ñ);
                            particiones[part2].ProCargado = p_mascorto;
                            particiones[part2].libre = false;
                            
                            }
                            
                }
    }
    public void cargarCPU() {
        PlanCortoPlazo();
        
        if(!colaListo.isEmpty()){
        if (cpu[0].libre){  //
            for (int i=0;i<colaListo.size();i++){
                for (int j=0;j<3;j++){
                if(p_mascorto==colaListo.get(i).PID && particiones[j].ProCargado==p_mascorto){
                        cpu[0].PID = colaListo.get(i).getPID();
                        cpu[0].Tam = colaListo.get(i).getTam();
                        cpu[0].TA = colaListo.get(i).getTA();
                        cpu[0].NroPart = j;
                        cpu[0].libre = false;
                        tpp=colaListo.get(i).TI;
                         
                        tiempoproceso=colaListo.get(i).TI;
                        if(cpu[0].NroPart == 0){pb1.setVisible(true);}
                        if(cpu[0].NroPart == 1){pb2.setVisible(true);}
                        if(cpu[0].NroPart == 2){pb3.setVisible(true);}
                        //colaListo.remove(i);
                        
                        String m="       ";
                        if (!colaListo.isEmpty()){
                        for (int l=0;l<colaListo.size();l++){
                        m += "       | "+colaListo.get(l).PID+" |";
                        }}
                        cl.setText("Cola de Listos: "+m);
                        if (cpu[0].NroPart == 0){p1.setText("P"+particiones[0].ProCargado+" en ejecucion"+" FI: "+Fr1);}
                        if (cpu[0].NroPart == 1){p2.setText("P"+particiones[1].ProCargado+ " en ejecucion"+" FI: "+Fr2);}
                        if (cpu[0].NroPart == 2){p3.setText("P"+particiones[2].ProCargado+ " en ejecucion"+" FI "+Fr3);}
                        //i--;
                }
        }}
        
        }
    }}
    
    
    public void TerminarProceso(int PID){
        for (int k=0;k<3;k++) {
               if((particiones[k].ProCargado)==(PID)){
                   particiones[k].ProCargado = 0;
                   particiones[k].libre = true;
                   
                   proceso1 = new Proceso(PID, 0,cpu[0].Tam,5,0);
                   if(PID!=0){
                   colaTerminados.add(proceso1);    //agrego el proceso a la lista de procesos terminados
                   //if(!colaListoSuspendido.isEmpty()){
                   //Proceso u = colaListoSuspendido.get(0); //busco el primer proceso suspendido y
                   //colaListo.add(u);                          //lo mando a la cola de listos
                   //colaListoSuspendido.remove(0);}
                   
                   
                   if (cpu[0].NroPart == 0){p1.setText("ESPACIO LIBRE");}
                   if (cpu[0].NroPart == 1){p2.setText("ESPACIO LIBRE");}
                   if (cpu[0].NroPart == 2){p3.setText("ESPACIO LIBRE");}
                   cpu[0].PID = 0;
                   cpu[0].Tam = 0;
                   cpu[0].NroPart=-1;
                   cpu[0].libre=true;
                   int m=100;
                   if (!colaListo.isEmpty()){
                        for (int l=0;l<colaListo.size();l++){
                            if(colaListo.get(l).PID == PID){
                        m = l;}
                        }}
                   if (m!=100){
                   colaListo.remove(m);}
                   }
                
               }
           }
    int instante = inst;
    boolean cargoo = false,b1=false,b2=false,b3=false;
    if(!colaListo.isEmpty()){
    //PlanCortoPlazo();
    //cargaWorstFit(colaListo.get(p_mascorto)); 
    ejecutar2();}
    
    }
    
    
              
    public Icon icono(String path, int width, int heigth){
        Icon img = new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage()
        .getScaledInstance(width, heigth, java.awt.Image.SCALE_SMOOTH));
        return img;
    }
    private void Carga(){
        leer();
        if(bul == true){
        leer();
        }
        
        
        
        
    }
    private void Temporizador(){
    Timer timer = new Timer (3000, new ActionListener ()
        {
            
        public void actionPerformed(ActionEvent e)
        {
                                                        // Aquí el código que queramos ejecutar.
        
        }
        });
        timer.setInitialDelay(0);
        timer.setRepeats(true);
        timer.start();}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        min = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        Menu = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        p4 = new javax.swing.JLabel();
        p3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        p5 = new javax.swing.JLabel();
        p2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        p6 = new javax.swing.JLabel();
        p1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        pb2 = new javax.swing.JProgressBar();
        pb3 = new javax.swing.JProgressBar();
        pb1 = new javax.swing.JProgressBar();
        p = new javax.swing.JTextField();
        cn1 = new javax.swing.JTextField();
        cn = new javax.swing.JTextField();
        nuevo = new javax.swing.JButton();
        acerca = new javax.swing.JButton();
        cl = new javax.swing.JTextField();
        cls = new javax.swing.JTextField();
        cf = new javax.swing.JTextField();
        ayuda = new javax.swing.JButton();
        mafos = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        min.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        min.setForeground(new java.awt.Color(255, 255, 255));
        min.setText("__");
        min.setBorder(null);
        min.setBorderPainted(false);
        min.setContentAreaFilled(false);
        min.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minMouseExited(evt);
            }
        });
        min.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minActionPerformed(evt);
            }
        });
        getContentPane().add(min, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 10, -1, -1));

        exit.setFont(new java.awt.Font("Trebuchet MS", 0, 48)); // NOI18N
        exit.setForeground(new java.awt.Color(255, 255, 255));
        exit.setText("X");
        exit.setBorder(null);
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitMousePressed(evt);
            }
        });
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        getContentPane().add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 0, 30, 40));

        Menu.setMaximumSize(new java.awt.Dimension(1030, 770));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(51, 51, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 255)));
        jPanel4.setForeground(new java.awt.Color(153, 255, 255));
        jPanel4.setToolTipText("");
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p4.setBackground(new java.awt.Color(153, 255, 255));
        p4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p4.setForeground(new java.awt.Color(153, 255, 255));
        p4.setText("250KB");
        jPanel4.add(p4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, -1, -1));

        p3.setBackground(new java.awt.Color(153, 255, 255));
        p3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p3.setForeground(new java.awt.Color(153, 255, 255));
        p3.setText("ESPACIO LIBRE");
        jPanel4.add(p3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        jLabel16.setBackground(new java.awt.Color(153, 255, 255));
        jLabel16.setForeground(new java.awt.Color(153, 255, 255));
        jLabel16.setText("530");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, -1));

        jLabel13.setBackground(new java.awt.Color(153, 255, 255));
        jLabel13.setForeground(new java.awt.Color(153, 255, 255));
        jLabel13.setText("281");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));

        jLabel9.setBackground(new java.awt.Color(153, 255, 255));
        jLabel9.setForeground(new java.awt.Color(153, 255, 255));
        jLabel9.setText("P3");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        Menu.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 280, 120));

        jPanel2.setBackground(new java.awt.Color(51, 51, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 255)));
        jPanel2.setForeground(new java.awt.Color(153, 255, 255));
        jPanel2.setToolTipText("");
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p5.setBackground(new java.awt.Color(153, 255, 255));
        p5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p5.setForeground(new java.awt.Color(153, 255, 255));
        p5.setText("120KB");
        jPanel2.add(p5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, -1, -1));

        p2.setBackground(new java.awt.Color(153, 255, 255));
        p2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p2.setForeground(new java.awt.Color(153, 255, 255));
        p2.setText("ESPACIO LIBRE");
        jPanel2.add(p2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        jLabel12.setBackground(new java.awt.Color(153, 255, 255));
        jLabel12.setForeground(new java.awt.Color(153, 255, 255));
        jLabel12.setText("280");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, -1));

        jLabel15.setBackground(new java.awt.Color(153, 255, 255));
        jLabel15.setForeground(new java.awt.Color(153, 255, 255));
        jLabel15.setText("161");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));

        jLabel10.setBackground(new java.awt.Color(153, 255, 255));
        jLabel10.setForeground(new java.awt.Color(153, 255, 255));
        jLabel10.setText("P2");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        Menu.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 280, 120));

        jPanel3.setBackground(new java.awt.Color(51, 51, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 255)));
        jPanel3.setForeground(new java.awt.Color(153, 255, 255));
        jPanel3.setToolTipText("");
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p6.setBackground(new java.awt.Color(153, 255, 255));
        p6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p6.setForeground(new java.awt.Color(153, 255, 255));
        p6.setText("60KB");
        jPanel3.add(p6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, -1, -1));

        p1.setBackground(new java.awt.Color(153, 255, 255));
        p1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p1.setForeground(new java.awt.Color(153, 255, 255));
        p1.setText("ESPACIO LIBRE");
        jPanel3.add(p1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        jLabel11.setBackground(new java.awt.Color(153, 255, 255));
        jLabel11.setForeground(new java.awt.Color(153, 255, 255));
        jLabel11.setText("160");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, -1));

        jLabel14.setBackground(new java.awt.Color(153, 255, 255));
        jLabel14.setForeground(new java.awt.Color(153, 255, 255));
        jLabel14.setText("101");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));

        jLabel8.setBackground(new java.awt.Color(153, 255, 255));
        jLabel8.setForeground(new java.awt.Color(153, 255, 255));
        jLabel8.setText("P1");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        Menu.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 280, 120));

        jPanel1.setBackground(new java.awt.Color(51, 0, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 255)));
        jPanel1.setForeground(new java.awt.Color(153, 255, 255));
        jPanel1.setToolTipText("");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(153, 255, 255));
        jLabel2.setForeground(new java.awt.Color(153, 255, 255));
        jLabel2.setText("100");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, -1));

        jLabel4.setBackground(new java.awt.Color(153, 255, 255));
        jLabel4.setForeground(new java.awt.Color(153, 255, 255));
        jLabel4.setText("P0");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        jLabel5.setBackground(new java.awt.Color(153, 255, 255));
        jLabel5.setForeground(new java.awt.Color(153, 255, 255));
        jLabel5.setText("0");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, -1, -1));

        jLabel7.setBackground(new java.awt.Color(153, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 255, 255));
        jLabel7.setText("SISTEMA OPERATIVO 100KB");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        Menu.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 610, 280, 120));

        tabla.setBackground(new java.awt.Color(51, 51, 255));
        tabla.setFont(new java.awt.Font("Sitka Text", 0, 14)); // NOI18N
        tabla.setForeground(new java.awt.Color(153, 255, 255));
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_PROCESO", "TAMAÑO (Kilobytes)", "TA (segundos)", "TI (segundos)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.setGridColor(new java.awt.Color(153, 255, 255));
        tabla.setSelectionBackground(new java.awt.Color(102, 255, 255));
        tabla.setSelectionForeground(new java.awt.Color(0, 0, 255));
        tabla.getTableHeader().setResizingAllowed(false);
        tabla.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabla);

        Menu.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, 600, 210));

        jButton2.setBackground(new java.awt.Color(51, 51, 255));
        jButton2.setForeground(new java.awt.Color(153, 255, 255));
        jButton2.setText("Guardar cambios");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Menu.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 170, -1, -1));

        pb2.setBackground(new java.awt.Color(51, 51, 255));
        pb2.setForeground(new java.awt.Color(153, 255, 255));
        pb2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Menu.add(pb2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 280, 40));

        pb3.setBackground(new java.awt.Color(51, 51, 255));
        pb3.setForeground(new java.awt.Color(153, 255, 255));
        pb3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Menu.add(pb3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 280, 40));

        pb1.setBackground(new java.awt.Color(51, 51, 255));
        pb1.setForeground(new java.awt.Color(153, 255, 255));
        pb1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Menu.add(pb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 280, 40));

        p.setEditable(false);
        p.setBackground(new java.awt.Color(51, 51, 255));
        p.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        p.setForeground(new java.awt.Color(153, 255, 255));
        p.setText("EMPEZAR");
        Menu.add(p, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 220, 40));

        cn1.setEditable(false);
        cn1.setBackground(new java.awt.Color(51, 51, 255));
        cn1.setForeground(new java.awt.Color(153, 255, 255));
        cn1.setText("Cola de Nuevos: ");
        Menu.add(cn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 500, 580, 40));

        cn.setEditable(false);
        cn.setBackground(new java.awt.Color(51, 51, 255));
        cn.setForeground(new java.awt.Color(153, 255, 255));
        cn.setText("Procesos sin arribar: ");
        Menu.add(cn, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 440, 580, 40));

        nuevo.setBackground(new java.awt.Color(51, 51, 255));
        nuevo.setForeground(new java.awt.Color(153, 255, 255));
        nuevo.setText("NUEVO");
        nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nuevoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nuevoMouseExited(evt);
            }
        });
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });
        Menu.add(nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 70, 30));

        acerca.setBackground(new java.awt.Color(51, 51, 255));
        acerca.setForeground(new java.awt.Color(153, 255, 255));
        acerca.setText("ACERCA DE...");
        acerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acercaActionPerformed(evt);
            }
        });
        Menu.add(acerca, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 110, 30));

        cl.setEditable(false);
        cl.setBackground(new java.awt.Color(51, 51, 255));
        cl.setForeground(new java.awt.Color(153, 255, 255));
        cl.setText("Cola de Listos: ");
        Menu.add(cl, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 570, 580, 40));

        cls.setEditable(false);
        cls.setBackground(new java.awt.Color(51, 51, 255));
        cls.setForeground(new java.awt.Color(153, 255, 255));
        cls.setText("Cola de Listos y Suspendidos: ");
        Menu.add(cls, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 630, 580, 40));

        cf.setEditable(false);
        cf.setBackground(new java.awt.Color(51, 51, 255));
        cf.setForeground(new java.awt.Color(153, 255, 255));
        cf.setText("Cola de Terminados: ");
        Menu.add(cf, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 690, 580, 40));

        ayuda.setBackground(new java.awt.Color(51, 51, 255));
        ayuda.setForeground(new java.awt.Color(153, 255, 255));
        ayuda.setText("AYUDA");
        ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ayudaActionPerformed(evt);
            }
        });
        Menu.add(ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 70, 30));

        mafos.setText("MaFosDev - All rights reserved. 2022");
        mafos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mafosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mafosMouseExited(evt);
            }
        });
        Menu.add(mafos, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 750, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setText("SIMULADOR SISTEMAS OPERATIVOS");
        Menu.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/asd.jpg"))); // NOI18N
        Menu.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 770));

        jButton3.setBackground(new java.awt.Color(51, 51, 255));
        jButton3.setForeground(new java.awt.Color(153, 255, 255));
        jButton3.setText("Eliminar seleccionado");
        Menu.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, -1, -1));

        getContentPane().add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void minMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minMouseEntered
        min.setForeground(Color.gray);
    }//GEN-LAST:event_minMouseEntered

    private void minMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minMouseExited
        // TODO add your handling code here:
        min.setForeground(Color.WHITE);
    }//GEN-LAST:event_minMouseExited

    private void minActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minActionPerformed
        // TODO add your handling code here:
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_minActionPerformed

    private void exitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseEntered
        // TODO add your handling code here:
        exit.setForeground(Color.gray);
    }//GEN-LAST:event_exitMouseEntered

    private void exitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseExited
        // TODO add your handling code here:
        exit.setForeground(Color.WHITE);
    }//GEN-LAST:event_exitMouseExited

    private void exitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_exitMousePressed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        UI = null;
        UI.put("Panel.background",new Color(100,100,100));
        UI.put("OptionPane.background",new Color(100,100,100));
        UI.put("OptionPane.messageForeground",Color.BLACK);
        Object [] options = {icono("/img/cheque.png",40,40), icono("/img/cancelar.png",40,40)};
        int confirmar = JOptionPane.showOptionDialog(null, "¿Desea salir?", "", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icono("", 10, 10), options, options[0]);
        if(confirmar == JOptionPane.YES_OPTION){
            System.exit(0);}
    }//GEN-LAST:event_exitActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        modificar();
        CargarProcesos();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        particiones[0].ProCargado= 0;particiones[0].libre=true;
        particiones[1].ProCargado= 0;particiones[1].libre=true;
        particiones[2].ProCargado= 0;particiones[2].libre=true;
        pb1.setVisible(false);
        pb2.setVisible(false);
        pb3.setVisible(false);
        cpu[0].NroPart=-1;
        cpu[0].PID=0;
        cpu[0].TA=0;
        cpu[0].Tam=0;
        cpu[0].libre=true;
        tiempoproceso=0;
        x=0;inst= -1;multip=0;mascorto=100;p_mascorto=0;tiempoproceso =0;tpp=0;xo=0;
        p.setText("EMPEZAR");
        Fr1=0;Fr2=0;Fr3=0;f1=0;f2=0;f3=0;
        cargop=false;bul=false;
        if(!colaListo.isEmpty()){for (int i = 0; i<colaListo.size();i++){
        colaListo.remove(i);i--;}}
        if(!colaNuevo.isEmpty()){for (int i = 0; i<colaNuevo.size();i++){
        colaNuevo.remove(i);i--;}}
        if(!colaListoSuspendido.isEmpty()){for (int i = 0; i<colaListoSuspendido.size();i++){
        colaListoSuspendido.remove(i);i--;}}
        if(!colaTerminados.isEmpty()){for (int i = 0; i<colaTerminados.size();i++){
        colaTerminados.remove(i);i--;}}
        if(!colaVivos.isEmpty()){for (int i = 0; i<colaVivos.size();i++){
        colaVivos.remove(i);i--;}}
        if(!lista.isEmpty()){for (int i = 0; i<lista.size();i++){
        lista.remove(i);i--;}}
        UIManager.put( "nimbusOrange", new Color( 38, 139, 210 ) );
        cn1.setText("Cola de Nuevos: ");
        cl.setText("Cola de Listos: ");
        cls.setText("Cola de Listos y Suspendidos: ");
        cn.setText("Procesos sin arribar: ");
        cf.setText("Cola de Terminados: ");
        //CargarExcel();
        //Carga();
        //crearMemoriaFija();
        CargarProcesos();
        
    }//GEN-LAST:event_nuevoActionPerformed

    private void acercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acercaActionPerformed
        acercade m = new acercade();
        m.setVisible(true);
    }//GEN-LAST:event_acercaActionPerformed

    private void ayudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ayudaActionPerformed
        // TODO add your handling code here:
        ayuda m = new ayuda();
        m.setVisible(true);
    }//GEN-LAST:event_ayudaActionPerformed

    private void mafosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mafosMouseEntered
        // TODO add your handling code here:
        mafos.setForeground(Color.white);
    }//GEN-LAST:event_mafosMouseEntered

    private void mafosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mafosMouseExited
        // TODO add your handling code here:
        mafos.setForeground(Color.black);
    }//GEN-LAST:event_mafosMouseExited

    private void nuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoMouseEntered
        nuevo.setMinimumSize(new Dimension(90,50));
        //nuevo.setMaximumSize(new Dimension(90,50));
        
    }//GEN-LAST:event_nuevoMouseEntered

    private void nuevoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoMouseExited
        nuevo.setMinimumSize(new Dimension(70,30));
        //nuevo.setMaximumSize(new Dimension(70,30));
    }//GEN-LAST:event_nuevoMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JButton acerca;
    private javax.swing.JButton ayuda;
    private javax.swing.JTextField cf;
    private javax.swing.JTextField cl;
    private javax.swing.JTextField cls;
    private javax.swing.JTextField cn;
    private javax.swing.JTextField cn1;
    private javax.swing.JButton exit;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel mafos;
    private javax.swing.JButton min;
    private javax.swing.JButton nuevo;
    private javax.swing.JTextField p;
    private javax.swing.JLabel p1;
    private javax.swing.JLabel p2;
    private javax.swing.JLabel p3;
    private javax.swing.JLabel p4;
    private javax.swing.JLabel p5;
    private javax.swing.JLabel p6;
    private javax.swing.JProgressBar pb1;
    private javax.swing.JProgressBar pb2;
    private javax.swing.JProgressBar pb3;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
