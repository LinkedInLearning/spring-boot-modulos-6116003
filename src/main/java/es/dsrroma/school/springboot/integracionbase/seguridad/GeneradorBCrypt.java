package es.dsrroma.school.springboot.integracionbase.seguridad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneradorBCrypt {
    public static void main(String[] args) {
        String contrasenya = "abc123";
        String hash = new BCryptPasswordEncoder().encode(contrasenya);
        System.out.println(hash);
    }
}
