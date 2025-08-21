package es.dsrroma.school.springboot.integracionbase.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.dsrroma.school.springboot.integracionbase.dtos.LoginDTO;
import es.dsrroma.school.springboot.integracionbase.seguridad.jwt.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired 
    private AuthenticationManager authManager;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        // conservamos la variablea auth (podríamos quitar la asignación, no la llamada
        // para poder saber quién se autenticó

        String jwt = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }
}
