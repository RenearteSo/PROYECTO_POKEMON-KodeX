package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Entrenador;
import model.Estado;
import model.Pokemon;

public class LigaDao {

    private CombateDao combateDao = new CombateDao();

 // Devuelve la lista de 4 entrenadores del Alto Mando desordenados
    public List<Entrenador> generarAltoMando(int nivelJugador) {
        List<Entrenador> altoMando = new ArrayList<>();
        
        int nivelMin = Math.max(1, nivelJugador - 2);
        int nivelMax = nivelJugador + 5;

        Entrenador e1 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e1.setNombreEntrenador("Scientist (Alto Mando)");
        e1.setTipoEntrenador("ALTO_MANDO");
        e1.setRutaImgFrontal("/img/Entrenador/Scientist_ligaPokemon.png");

        Entrenador e2 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e2.setNombreEntrenador("Chuck (Alto Mando)");
        e2.setTipoEntrenador("ALTO_MANDO");
        e2.setRutaImgFrontal("/img/Entrenador/Chuck_ligaPokemon.png");

        Entrenador e3 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e3.setNombreEntrenador("Baker (Alto Mando)");
        e3.setTipoEntrenador("ALTO_MANDO");
        e3.setRutaImgFrontal("/img/Entrenador/Baker_ligaPokemon.png");

        Entrenador e4 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e4.setNombreEntrenador("Ranger (Alto Mando)");
        e4.setTipoEntrenador("ALTO_MANDO");
        e4.setRutaImgFrontal("/img/Entrenador/Ranger_ligaPokemon.png");

        altoMando.add(e1); altoMando.add(e2); altoMando.add(e3); altoMando.add(e4);
        Collections.shuffle(altoMando); // Barajamos el orden cada vez
        
        return altoMando;
    }

    // genera al campeon (el combate 5)
    public Entrenador generarCampeon(Entrenador jugador) {
        Entrenador campeon = combateDao.generarRivalAleatorio(jugador.getNivelEntrenador() + 5, jugador.getNivelEntrenador() + 10, 6);
        campeon.setNombreEntrenador("Campeón Azul");
        campeon.setTipoEntrenador("CAMPEON");
        campeon.setRutaImgFrontal("/img/Entrenador/Blaine_ligaPokemon.png");
        return campeon;
    }
    
    // curación completa del equipo
    public void curarEquipoEntero(Entrenador e) {
        for(Pokemon p : e.getEquipoPrincipal()) {
            if(p != null) {
                int statBaseBD = 10;
                int maxHp = 15 + statBaseBD + (p.getNivel() * 3);
                
                p.setVitalidad(maxHp); 
                p.setEstamina(100);
                p.setEstadoActual(Estado.SALUDABLE);
            }
        }
    }
}