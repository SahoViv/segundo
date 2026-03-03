/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bolsa_de_trabajo;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Blob;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
/**
 *Alumnos: BERNAL MARTINEZ RICARDO y OLGUIN VIVIAN YARED SAHORI
 *Grupo: 3DSM5
 */
//CONEXIÓN A LA BASE DE DATOS
public class Metodos_bt{
    Connection conexion;
    String sql="";
    public void conectarbd(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/bolsa_trabajo","root","");
             System.out.println("Se conectó a la base de datos");
        }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
    }
    //AGREGAR POSTULANTE
    public void agregarpostulante(String nombre, String a_pat, String a_mat, String correo_p, String matricula,
                                  String carrera, String opcion_de_modelo){
        try{
            System.out.println("Datos obtenidos:\t"+nombre);
            conectarbd();
            sql="Insert into postulante(nombre, a_pat, a_mat, correo_p, matricula, carrera, opcion_de_modelo)"
                + "values(?,?,?,?,?,?,?)";
            PreparedStatement pre=conexion.prepareStatement(sql);
                pre.setString(1, nombre);
                pre.setString(2, a_pat);
                pre.setString(3, a_mat);
                pre.setString(4, correo_p);
                pre.setString(5, matricula);
                pre.setString(6,carrera);
                pre.setString(7,opcion_de_modelo);
                pre.executeUpdate();
            System.out.println("Se agregó el usuario");
        }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
    }
    //AGREGAR VACANTE
    public void agregarvacante(String nombre_empresa, String puesto_vacante, String ubi_v, String sueldo, 
                               String descripcion_v){
        try{
            System.out.println("Datos obtenidos:\t"+nombre_empresa);
            conectarbd();
            sql="Insert Into vacante(nombre_empresa, puesto_vacante, ubi_v, descripcion_v, sueldo)values(?,?,?,?,?)";
            PreparedStatement pre=conexion.prepareStatement(sql);
                pre.setString(1, nombre_empresa);
                pre.setString(2, puesto_vacante);
                pre.setString(3, ubi_v);
                pre.setString(4, sueldo);
                pre.setString(5, descripcion_v);
                pre.executeUpdate();
            System.out.println("Se agregó la vacante");
        }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
    }
    //RELLENA EL JCOMBO BOX CON LAS VACANTES
    public void rellenarvacantes(JComboBox combo){
        try{
            conectarbd();
            sql="SELECT puesto_vacante FROM vacante";
                PreparedStatement ps= conexion.prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                while(rs.next()){
                combo.addItem(rs.getString("puesto_vacante"));
                }
                rs.close();
                ps.close();
                conexion.close();
        }catch(Exception basura){
             System.out.println("Error:\t"+basura);
        }  
     }
    //MUESTRA LOS DATOS DE LA VACANTE EN LOS JTEXTFIELD
    public void mostrarDetallesVacante(String vacanteSel,JTextField nombre, JTextField puesto,JTextField ubi,
                                       JTextField sueldo,JTextField descripcion, JTextField identificador){
        try {
            conectarbd();
            sql="SELECT id_vacante, nombre_empresa, puesto_vacante, ubi_v, sueldo, descripcion_v "
                +"FROM vacante WHERE TRIM(puesto_vacante)=?";
                    PreparedStatement ps = conexion.prepareStatement(sql);
                    ps.setString(1, vacanteSel);
                        System.out.println("Datos obtenidos:\t"+vacanteSel);
                    ResultSet rs = ps.executeQuery();
                        if (rs.next()){
                            System.out.println("Registro hallado");   
                            identificador.setText(rs.getString("id_vacante"));
                            nombre.setText(rs.getString("nombre_empresa"));
                            puesto.setText(rs.getString("puesto_vacante"));
                            ubi.setText(rs.getString("ubi_v"));
                            sueldo.setText(rs.getString("sueldo"));
                            descripcion.setText(rs.getString("descripcion_v"));
                        }else{
                            System.out.println("No encontrado");
                                JOptionPane.showMessageDialog(null,"No se encontró información para:"
                                                              +vacanteSel,"Invalido",JOptionPane.INFORMATION_MESSAGE);
                        }
                        rs.close();
                        ps.close();
                        conexion.close();
            } catch (Exception basura){
                System.out.println("Error:\t"+basura);
        }
}
    //RADIO BUTON DEL REGISTRO
    public void nivel(String opcion_modelo){
        try{
            System.out.println("Datos obtenidos:\t"+opcion_modelo);
            conectarbd();
            sql="Insert into carreras(nombre_carrera, division, nombre_director) values(?,?,?)";
                PreparedStatement pre=conexion.prepareStatement(sql);
                    pre.setString(1, opcion_modelo);
                    pre.executeUpdate();
                    System.out.println("Se agrego la carrera");
        }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
}
    //MODIFICAR LOS DATOS DE LAS VACANTES
    public void modificar(String nombre_empresa, String puesto_vacante, String ubi_v, 
                          String descripcion_v, String sueldo, String id_vacante){
        int confirmar=JOptionPane.showConfirmDialog(null,"¿Desea modificar los datos actuales?",
                                                    "Importante",JOptionPane.INFORMATION_MESSAGE);
        if(confirmar==JOptionPane.YES_OPTION){
            try{
                conectarbd();
                sql="UPDATE vacante SET nombre_empresa=?, puesto_vacante=?, ubi_v=?, descripcion_v=?,"
                    + " sueldo=? WHERE id_vacante=?";
                    PreparedStatement prest= conexion.prepareStatement(sql);
                        prest.setString(1, nombre_empresa);
                        prest.setString(2, puesto_vacante);
                        prest.setString(3, ubi_v);
                        prest.setString(4, descripcion_v);
                        prest.setString(5, sueldo);
                        prest.setString(6, id_vacante);
                System.out.println("Datos obtenidos:\t"+nombre_empresa);
                        prest.executeUpdate();
            if(prest.executeUpdate()>0){
                JOptionPane.showMessageDialog(null,"Los datos se han modificado","Operación Exitosa"
                                              ,JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null,"No se modificaron los datos","Invalido",JOptionPane.WARNING_MESSAGE);
            }
            }catch(Exception basura){
                System.out.println("Error:\t"+basura);
            }
        }  
    }
    //ELIMINAR VACANTES SELECCIONADAS
    public void eliminar(String id_vacante){
        int confirmar=JOptionPane.showConfirmDialog(null,"¿Desea eliminar la vacante actual?",
                                                    "Importante",JOptionPane.WARNING_MESSAGE);
        if(confirmar==JOptionPane.YES_OPTION){
            try{
                conectarbd();
                sql="Delete from vacante where id_vacante=?";
                    PreparedStatement prest= conexion.prepareStatement(sql);
                        prest.setString(1, id_vacante);
                System.out.println("Datos obtenidos:\t"+id_vacante);
                        prest.executeUpdate();
                System.out.println("Se eliminó la vacante");
                    if(prest.executeUpdate()==0){
                        JOptionPane.showMessageDialog(null,"Se eliminó la vacante",
                                                      "Operacion Exitosa",JOptionPane.INFORMATION_MESSAGE);
                    }else{
                    JOptionPane.showMessageDialog(null,"No se elimino la vacante","Invalido",JOptionPane.WARNING_MESSAGE);
                    }
            }catch(Exception basura){
                System.out.println("Error:\t"+basura);
        }
    }
}
    //INICIO DEL LOGIN
    public String[] buscar_postulante(String correo_p){
        String[] datos=new String[4];
        try{
        System.out.println("Datos obtenidos:\t"+correo_p);
            conectarbd();
            sql="select id_postulante, nombre, a_pat, a_mat from postulante where correo_p=?";
                PreparedStatement pre=conexion.prepareStatement(sql);
                pre.setString(1, correo_p);
                ResultSet rs=pre.executeQuery();
                    if(rs.next()){
                        datos[0] =rs.getString("id_postulante");
                        datos[1] =rs.getString("nombre");
                        datos[2] =rs.getString("a_pat");
                        datos[3] =rs.getString("a_mat");
                    }       
                    //
                    
                    
                    
                    
                    //
            for(int i=0;i<4;i++){
                    System.out.println("Datos obtenidos:\t"+datos[i]);//para que se hagan los datos por si solo
            }
        }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
    return datos;
}
    //COMPROBAR EL LOGIN CON LA BUSQUEDA DEL USUARIO
    public String buscar_postulante_registrado(String correo_p, String matricula){
        String busqueda_usuario=null;
        try{
            conectarbd();
            sql="select nombre, a_pat, a_mat from postulante where correo_p=? && matricula=?";
                PreparedStatement pre=conexion.prepareStatement(sql);
                    pre.setString(1, correo_p);
                    pre.setString(2, matricula);
                ResultSet rs=pre.executeQuery();
                    if(rs.next()){
                        busqueda_usuario ="Usuario encontrado";
                    }else{
                        busqueda_usuario = "Usuario no encontrado";
                    }
                    rs.close();
                    pre.close();
                    conexion.close();
        }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
    return busqueda_usuario;
}
    //LLENA LA TABLA DE tabla_v_p CON DATOS DEL POSTULANTE, VACANTE Y SU CV
    public void tabla(int id_vacante, int id_postulante, File cv){
        int confirmar =JOptionPane.showConfirmDialog(null,"Al subir tu Archivo PDF(CV),"
                                                     + " confimas la solicitud de la vacante seleccionada, ¿Deseas continuar?"
                                                     ,"Confirmación",JOptionPane.INFORMATION_MESSAGE);
        if(confirmar==JOptionPane.YES_OPTION){
            try{
                System.out.println("Datos obtenidos:\t"+id_vacante+" y \t"+id_postulante);
                conectarbd();
                sql="Insert into tabla_v_p(id_vacante, id_postulante, cv) values(?,?,?)";
                    PreparedStatement pre=conexion.prepareStatement(sql);
                        pre.setInt(1, id_vacante);
                        pre.setInt(2, id_postulante);
                        FileInputStream input = new FileInputStream(cv); 
                        pre.setBlob(3, input);
                        pre.executeUpdate();
            System.out.println("Se agregó a la tabla");
            }catch(Exception basura){
            System.out.println("Error:\t"+basura);
        }
    }         
}
    //MUESTRA LOS DATOS DE EL POSTULANTE Y LA VACANTE REGISTRADOS EN tabla_v_p
    public DefaultTableModel mostrar_datos_tabla(JTable tabla){
       List<Blob> listaArchivos = new ArrayList<>();//Guarda los BLOB´s en una sola columna
       String [] nombre_columnas={"Vacante", "Correo", "Matricula", "Carrera","Nivel Académico",  "CV(PDF)"};
       DefaultTableModel modelo=new DefaultTableModel(null,nombre_columnas);//Modelo de la tabla
            sql="SELECT puesto_vacante, correo_p, matricula, Carrera, opcion_de_modelo, cv FROM vacante"
                    + " INNER JOIN tabla_v_p ON vacante.id_vacante=tabla_v_p.id_vacante "
                    + "INNER JOIN postulante ON tabla_v_p.id_postulante=postulante.id_postulante";
       try{
           conectarbd();
                PreparedStatement pre=conexion.prepareStatement(sql);
                ResultSet rs=pre.executeQuery();
                    while(rs.next()){
                        String [] registros=new String[6];
                            registros[0]=rs.getString("puesto_vacante");
                            registros[1]=rs.getString("correo_p");
                            registros[2]=rs.getString("matricula");
                            registros[3]=rs.getString("carrera");
                            registros[4]=rs.getString("opcion_de_modelo");
                            Blob blob = rs.getBlob("cv");
                            registros[5]= (blob !=null)? "Ver CV(PDF)" : "Sin archivo";//Muestra un mensaje en su lugar
                    listaArchivos.add(blob); // Guarda el blob en la misma posición que la columna
                    modelo.addRow(registros);// Agrega las filas
                    for(int i=0;i<6;i++){
                             System.out.println("Datos obtenidos:\t"+registros[i]);
                        }
                    }
           tabla.setModel(modelo);//Muestra el contenido
           tabla.addMouseListener(new MouseAdapter(){
    //HACE CLIC PARA VER LOS PDF DE LOS POSTULANTES
    public void mouseClicked(MouseEvent e){//Función al momento de dar un clic
        int filas=tabla.rowAtPoint(e.getPoint());//Obtiene el registro de esa fila
        int columnas=tabla.columnAtPoint(e.getPoint());//Por columna
            if (columnas==5){// Columna de los archivos
                String valor=tabla.getValueAt(filas, columnas).toString();
                //Obtiene los valores de fila y columna y los convierte a String                
                if (valor.trim().equalsIgnoreCase("Ver CV(PDF)")){
                    //Comprueba si el valor de la celda y se procede a abrir el archivo                
                    Blob blob=listaArchivos.get(filas);//Obtiene el Blob
                    if (blob!=null) {
                        System.out.println("Blob en fila " + filas + ": " + listaArchivos.get(filas));
                        try {// Convertir Blob en archivo temporal
                            InputStream entrada=blob.getBinaryStream();//Conversión
                            File temporal=File.createTempFile("cv_", ".pdf");//Archivo temporal en archivo .pdf
                            FileOutputStream salida=new FileOutputStream(temporal);
                            byte[] buffer=new byte[4096];//Mínimo de tamaño
                            int bytesRead;
                                while ((bytesRead=entrada.read(buffer))!=-1) {
                                    salida.write(buffer, 0, bytesRead);//Lee el peso del archivo
                                }
                                entrada.close();
                                salida.close();
                            // Abrir el archivo de manera temporal en el escritorio
                                if (Desktop.isDesktopSupported()) {
                                    Desktop.getDesktop().open(temporal);
                                }else {
                                    System.out.println("Desktop no soportado");
                                }
                        }catch(Exception basura) {
                            System.out.println("Error"+basura);
                        }
                    }
                }
            }
    }
});          
        rs.close();
        pre.close();
        conexion.close();
       }catch(Exception basura){
           System.out.println("Error:\t"+basura);
       }
        return modelo;
   }
}