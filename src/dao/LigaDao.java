package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Entrenador;
import model.Estado;
import model.Pokemon;

public class LigaDao {

    private CombateDao combateDao = new CombateDao();

    // devuelve la lista de 4 entrenadores del Alto Mando desordenados
    public List<Entrenador> generarAltoMando(int nivelJugador) {
        List<Entrenador> altoMando = new ArrayList<>();
        
        int nivelMin = Math.max(1, nivelJugador - 2);
        int nivelMax = nivelJugador + 5;

        Entrenador e1 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e1.setNombreEntrenador("Lorelei (Alto Mando)");
        e1.setTipoEntrenador("ALTO_MANDO");

        Entrenador e2 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e2.setNombreEntrenador("Bruno (Alto Mando)");
        e2.setTipoEntrenador("ALTO_MANDO");

        Entrenador e3 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e3.setNombreEntrenador("Agatha (Alto Mando)");
        e3.setTipoEntrenador("ALTO_MANDO");

        Entrenador e4 = combateDao.generarRivalAleatorio(nivelMin, nivelMax, 6);
        e4.setNombreEntrenador("Lance (Alto Mando)");
        e4.setTipoEntrenador("ALTO_MANDO");

        altoMando.add(e1); altoMando.add(e2); altoMando.add(e3); altoMando.add(e4);
        Collections.shuffle(altoMando); // Barajamos el orden cada vez
        
        return altoMando;
    }

    // genera al campeón (El combate 5)
    public Entrenador generarCampeon(Entrenador jugador) {
        Entrenador campeon = combateDao.generarRivalAleatorio(jugador.getNivelEntrenador() + 5, jugador.getNivelEntrenador() + 10, 6);
        campeon.setNombreEntrenador("Campeón Azul");
        campeon.setTipoEntrenador("CAMPEON");
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