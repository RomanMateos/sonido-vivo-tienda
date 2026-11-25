package cl.duoc.blog.service;

import cl.duoc.blog.dto.BlogRequest;
import cl.duoc.blog.dto.BlogResponse;
import cl.duoc.blog.exception.ResourceNotFoundException;
import cl.duoc.blog.model.Blog;
import cl.duoc.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public List<BlogResponse> listarArticulos(String titulo) {
        List<Blog> articulos = (titulo != null && !titulo.isBlank())
                ? blogRepository.findByTituloContainingIgnoreCaseOrderByFechaDesc(titulo.trim())
                : blogRepository.findAllByOrderByFechaDesc();

        return articulos.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BlogResponse obtenerPorId(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artículo de blog no encontrado con ID: " + id));
        return toResponse(blog);
    }

    @Transactional
    public BlogResponse crearArticulo(BlogRequest request) {
        Blog blog = Blog.builder()
                .titulo(request.getTitulo().trim())
                .resumen(request.getResumen().trim())
                .parrafo1(request.getParrafo1().trim())
                .subtitulo1(request.getSubtitulo1().trim())
                .parrafo2(request.getParrafo2().trim())
                .subtitulo2(request.getSubtitulo2().trim())
                .parrafo3(request.getParrafo3().trim())
                .imagen(request.getImagen().trim())
                .cita(request.getCita().trim())
                .build();

        return toResponse(blogRepository.save(blog));
    }

    @Transactional
    public BlogResponse actualizarArticulo(Long id, BlogRequest request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artículo de blog no encontrado con ID: " + id));

        blog.setTitulo(request.getTitulo().trim());
        blog.setResumen(request.getResumen().trim());
        blog.setParrafo1(request.getParrafo1().trim());
        blog.setSubtitulo1(request.getSubtitulo1().trim());
        blog.setParrafo2(request.getParrafo2().trim());
        blog.setSubtitulo2(request.getSubtitulo2().trim());
        blog.setParrafo3(request.getParrafo3().trim());
        blog.setImagen(request.getImagen().trim());
        blog.setCita(request.getCita().trim());

        return toResponse(blogRepository.save(blog));
    }

    @Transactional
    public void eliminarArticulo(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Artículo de blog no encontrado con ID: " + id);
        }
        blogRepository.deleteById(id);
    }

    private BlogResponse toResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .titulo(blog.getTitulo())
                .fecha(blog.getFecha())
                .resumen(blog.getResumen())
                .parrafo1(blog.getParrafo1())
                .subtitulo1(blog.getSubtitulo1())
                .parrafo2(blog.getParrafo2())
                .subtitulo2(blog.getSubtitulo2())
                .parrafo3(blog.getParrafo3())
                .imagen(blog.getImagen())
                .cita(blog.getCita())
                .build();
    }
}
