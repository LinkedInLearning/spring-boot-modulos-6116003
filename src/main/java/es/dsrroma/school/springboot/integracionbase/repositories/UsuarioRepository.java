package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dsrroma.school.springboot.integracionbase.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
