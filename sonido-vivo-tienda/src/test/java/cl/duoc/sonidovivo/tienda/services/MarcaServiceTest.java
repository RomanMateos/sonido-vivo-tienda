package cl.duoc.sonidovivo.tienda.services;

import cl.duoc.sonidovivo.tienda.entities.Marca;
import cl.duoc.sonidovivo.tienda.exceptions.RecursoNoEncontradoException;
import cl.duoc.sonidovivo.tienda.repositories.MarcaRepository;
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
class MarcaServiceTest {

    @Mock
    private MarcaRepository repo;

    @InjectMocks
    private MarcaService service;

    @Test
    void listar_debeRetornarMarcas() {
        Marca m = new Marca();
        m.setId(1L);
        m.setNombre("Fender");
        when(repo.findAll()).thenReturn(List.of(m));

        List<Marca> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Fender", resultado.get(0).getNombre());
    }

    @Test
    void buscar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscar(99L));
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarMarca() {
        Marca m = new Marca();
        m.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(m));

        service.eliminar(1L);

        verify(repo, times(1)).delete(m);
    }
}
