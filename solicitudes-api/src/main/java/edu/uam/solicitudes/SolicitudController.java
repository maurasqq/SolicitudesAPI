package edu.uam.solicitudes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "http://localhost:5173")
public class SolicitudController {
    private final List<Solicitud> solicitudes = new ArrayList<>(List.of(
        new Solicitud(1L, "Revisar servidor", "PENDIENTE"),
        new Solicitud(2L, "Validar respaldo", "RESUELTA")
    ));
    private final AtomicLong secuencia = new AtomicLong(2);

    @GetMapping
    public synchronized List<Solicitud> listar() {
        return List.copyOf(solicitudes);
    }

    @PostMapping
    public synchronized ResponseEntity<Solicitud> crear(@RequestBody Map<String, String> datos) {
        String descripcion = datos.getOrDefault("descripcion", "").trim();
        if (descripcion.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria");
        }
        Solicitud nueva = new Solicitud(secuencia.incrementAndGet(), descripcion, "PENDIENTE");
        solicitudes.add(nueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public synchronized Solicitud actualizar(@PathVariable Long id,
                                              @RequestBody Map<String, String> datos) {
        for (int i = 0; i < solicitudes.size(); i++) {
            Solicitud actual = solicitudes.get(i);
            if (actual.id().equals(id)) {
                String estado = datos.getOrDefault("estado", "");
                if (!estado.equals("PENDIENTE") && !estado.equals("RESUELTA")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido");
                }
                Solicitud modificada = new Solicitud(id, actual.descripcion(), estado);
                solicitudes.set(i, modificada);
                return modificada;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada");
    }

    @DeleteMapping("/{id}")
    public synchronized ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminada = solicitudes.removeIf(s -> s.id().equals(id));
        if (!eliminada) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
