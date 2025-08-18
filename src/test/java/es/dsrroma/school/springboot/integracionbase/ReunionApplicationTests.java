package es.dsrroma.school.springboot.integracionbase;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import es.dsrroma.school.springboot.integracionbase.models.Reunion;
import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReunionApplicationTests {

	private static final String NUEVO_ASUNTO = "Nuevo Asunto";
	private static final long ID_NAVIDAD = 99L;
	private static final long ID_AYER = 100L;
	private static final long ID_MANANA = 101L;
	private static final String ASUNTO_NAVIDAD = "Reunión Navidad";
	private static final String ASUNTO_AYER = "Reunión ayer";
	private static final String ASUNTO_MANANA = "Reunión mañana";
	private static final ZonedDateTime FECHA_NAVIDAD = ZonedDateTime.of(2025, 12, 25, 13, 0, 0, 0, ZoneId.systemDefault());

	@Autowired
    TestRestTemplate restTemplate;

    @Test
    void testGetReunion99() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity("/reuniones/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(99);

        String asunto = documentContext.read("$.asunto");
        assertThat(asunto).isEqualTo(ASUNTO_NAVIDAD);
    }

    @Test
    void testGetNotFound() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity("/reuniones/1000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void testCrearNueva() {
        Reunion newReunion = new Reunion(ID_NAVIDAD, ASUNTO_NAVIDAD, FECHA_NAVIDAD);
        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("admin", "abc123")
                .postForEntity("/reuniones", newReunion, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewReunion = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity(locationOfNewReunion, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String asunto = documentContext.read("$.asunto");

        assertThat(id).isNotNull();
        assertThat(asunto).isEqualTo(ASUNTO_NAVIDAD);
    }

    @Test
    void testGetTodas() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity("/reuniones", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int reunionCount = documentContext.read("$.length()");
        assertThat(reunionCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder((int) ID_NAVIDAD, (int) ID_AYER, (int) ID_MANANA);

        JSONArray asuntos = documentContext.read("$..asunto");
        assertThat(asuntos).containsExactlyInAnyOrder(ASUNTO_NAVIDAD, ASUNTO_AYER, ASUNTO_MANANA);
    }

    @Test
    void testGetPagina() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity("/reuniones/page?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void testGetPaginaYOrden() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity("/reuniones/page?page=0&size=1&sort=asunto,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        String asunto = documentContext.read("$[0].asunto");
        assertThat(asunto).isEqualTo(ASUNTO_MANANA);
    }

    @Test
    void testUsuarioInventado() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("BAD-USER", "abc123")
                .getForEntity("/reuniones/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
                .withBasicAuth("admin", "BAD-PASSWORD")
                .getForEntity("/reuniones/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DirtiesContext
    void testActualizacion() {
        Reunion reunionUpdate = new Reunion(null, NUEVO_ASUNTO, null);
        HttpEntity<Reunion> request = new HttpEntity<>(reunionUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .exchange("/reuniones/99", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("admin", "abc123")
                .getForEntity("/reuniones/99", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String asunto = documentContext.read("$.asunto");
        assertThat(id).isEqualTo(99);
        assertThat(asunto).isEqualTo(NUEVO_ASUNTO);
    }

    @Test
    void testActualizacionNotFound() {
        Reunion unknownCard = new Reunion(null, NUEVO_ASUNTO, null);
        HttpEntity<Reunion> request = new HttpEntity<>(unknownCard);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("admin", "abc123")
                .exchange("/reuniones/99999", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}