package cl.duoc.sonidovivo.tienda.services;

import cl.duoc.sonidovivo.tienda.entities.Producto;
import cl.duoc.sonidovivo.tienda.exceptions.RecursoNoEncontradoException;
import cl.duoc.sonidovivo.tienda.repositories.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void listar_debeRetornarProductos() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setNombre("Stratocaster");
        when(repository.findAll()).thenReturn(List.of(p));

        List<Producto> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Stratocaster", resultado.get(0).getNombre());
    }

    @Test
    void buscar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscar(50L));
    }

    @Test
    void actualizar_debeModificarCamposYGuardar() {
        Producto existente = new Producto();
        existente.setIdProducto(1L);
        existente.setNombre("Antiguo");
        existente.setStock(5);

        Producto nuevosDatos = new Producto();
        nuevosDatos.setNombre("Actualizado");
        nuevosDatos.setStock(10);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto actualizado = service.actualizar(1L, nuevosDatos);

        assertEquals("Actualizado", actualizado.getNombre());
        assertEquals(10, actualizado.getStock());
    }
}
