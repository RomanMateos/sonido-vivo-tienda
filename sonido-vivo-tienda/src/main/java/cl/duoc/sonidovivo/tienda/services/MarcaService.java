package cl.duoc.sonidovivo.tienda.services;

import cl.duoc.sonidovivo.tienda.entities.Marca;
import cl.duoc.sonidovivo.tienda.exceptions.RecursoNoEncontradoException;
import cl.duoc.sonidovivo.tienda.repositories.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {
    private final MarcaRepository repo;

    public MarcaService(MarcaRepository repo) {
        this.repo = repo;
    }

    public List<Marca> listar() {
        return repo.findAll();
    }

    public Marca buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada con id: " + id));
    }

    public Marca guardar(Marca marca) {
        return repo.save(marca);
    }

    public Marca actualizar(Long id, Marca datos) {
        Marca existente = buscar(id);
        existente.setNombre(datos.getNombre());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        Marca existente = buscar(id);
        repo.delete(existente);
    }
}