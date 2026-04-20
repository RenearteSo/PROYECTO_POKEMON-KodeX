package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Log {

    private String nombre;
    private LinkedList<String> listaTurnos;

    public Log() {
        // lista donde iremos metiendo el texto
        this.listaTurnos = new LinkedList<>();
        
        // nombre unico para el archivo usando la fecha y hora exacta
        DateTimeFormatter formatterFichero = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.nombre = LocalDateTime.now().format(formatterFichero) + ".log";
    }

    // MÉTODOS GET Y SET 
    
    public String getNombre() {
        return nombre;
    }

    public LinkedList<String> getListaTurnos() {
        return listaTurnos;
    }
    
    // vamos guardando lo que pasa en cada turno en la memoria antes de pasarlo al archivo
    public void registrarAccion(String codigo, Pokemon pJugador, Pokemon pRival, Entrenador jugador, Entrenador rival, int turno) {
        
        // pillamos la informacion de nuestro pokemon. Le ponemos valores por defecto si esta null para que no crashee
        String moteJ = (pJugador != null) ? pJugador.getMote() : "Ninguno";
        int nivelJ = (pJugador != null) ? pJugador.getNivel() : 0;
        String estadoJ = (pJugador != null && pJugador.getVitalidad() > 0) ? "OK" : "KO";

        // hacemos lo mismo para sacar los datos del rival
        String moteR = (pRival != null) ? pRival.getMote() : "Ninguno";
        int nivelR = (pRival != null) ? pRival.getNivel() : 0;
        String estadoR = (pRival != null && pRival.getVitalidad() > 0) ? "OK" : "KO";

        // sacamos la hora apara el txt
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        // hacemos la linea entera uniendo toda la información con el formato exacto que necesitamos
        String linea = fechaActual + " INFO " + codigo +
                " pokemon = {\"" + moteJ + "\", " + nivelJ + ", " + jugador.getNombreEntrenador() + ", " + estadoJ + "}, " +
                "pokemonRival= {\"" + moteR + "\", " + nivelR + ", " + rival.getNombreEntrenador() + ", " + estadoR + "}, " +
                "turno=" + turno;

        // la metemos en nuestra lista interna
        this.listaTurnos.add(linea);
    }

    // cuando acaba la pelea, guardamos todo lo que tenemos al archivo
    public void guardarFichero() {
        
        // comprobamos si existe la carpeta "logs" en el proyecto, y si no existe, la creamos
        File dir = new File("logs");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            // abrimos los canales de escritura usando BufferedWriter y PrintWriter
            FileWriter fw = new FileWriter("logs/" + this.nombre);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);

            // recorremos lo que hemos ido guardando y lo vamos imprimiendo línea a línea en el documento
            for (String linea : this.listaTurnos) {
                salida.println(linea);
            }

            salida.close();
            System.out.println("Log guardado correctamente en: logs/" + this.nombre);
            
        } catch (IOException e) {
            System.out.println("Error escribiendo los datos o cerrando el fichero: " + e.getMessage());
        }
    }
}