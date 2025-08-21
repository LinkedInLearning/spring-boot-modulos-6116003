package es.dsrroma.school.springboot.integracionbase.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {
	
    private final UsuarioDetailsServiceImpl usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsServiceImpl usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/auth/login").permitAll()
//				.requestMatchers(
//	                    "/v3/api-docs/**",
//	                    "/swagger-ui/**",
//	                    "/swagger-ui.html",
//	                    "/reuniones/**",
//	                    "/actas/**",
//	                    "/personas/**",
//	                    "/salas/**").authenticated())
				.anyRequest().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.formLogin(form -> form.permitAll());
		return http.build();
	}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
    		throws Exception {
        return config.getAuthenticationManager();
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
//		User.UserBuilder users = User.builder();
//		UserDetails admin = users.username("admin").password(passwordEncoder.encode("abc123")).build();
//		return new InMemoryUserDetailsManager(admin);
//	}
}
