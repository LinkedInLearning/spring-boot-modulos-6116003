package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import es.dsrroma.school.springboot.integracionbase.models.Usuario;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Usuario crear(Usuario usuario);
    void eliminar(Long id);
    Usuario buscarPorId(Long id);
}
