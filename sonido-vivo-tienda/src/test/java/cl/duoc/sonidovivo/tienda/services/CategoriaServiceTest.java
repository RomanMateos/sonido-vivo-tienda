package cl.duoc.sonidovivo.tienda.services;

import cl.duoc.sonidovivo.tienda.entities.Categoria;
import cl.duoc.sonidovivo.tienda.exceptions.RecursoNoEncontradoException;
import cl.duoc.sonidovivo.tienda.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @Test
    void listar_debeRetornarListaCategorias() {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("Guitarras");
        when(repository.findAll()).thenReturn(List.of(c));

        List<Categoria> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Guitarras", resultado.get(0).getNombre());
    }

    @Test
    void buscar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscar(99L));
    }

    @Test
    void guardar_debePersistirCategoria() {
        Categoria c = new Categoria();
        c.setNombre("Bajos");
        when(repository.save(c)).thenReturn(c);

        Categoria guardada = service.guardar(c);

        assertNotNull(guardada);
        assertEquals("Bajos", guardada.getNombre());
    }
}
