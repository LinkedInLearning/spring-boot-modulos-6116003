package es.dsrroma.school.springboot.integracionbase.seguridad;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.models.Usuario;
import es.dsrroma.school.springboot.integracionbase.repositories.UsuarioRepository;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<String> roles = Collections.emptyList();

        return new UsuarioDetails(usuario, roles);
    }
}
