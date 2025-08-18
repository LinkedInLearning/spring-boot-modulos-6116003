package es.dsrroma.school.springboot.integracionbase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import es.dsrroma.school.springboot.integracionbase.models.Reunion;

@JsonTest
class ReunionJsonTest {

	private static final long ID_NAVIDAD = 99L;
	private static final long ID_AYER = 100L;
	private static final long ID_MANANA = 101L;
	private static final String ASUNTO_NAVIDAD = "Reunión Navidad";
	private static final String ASUNTO_AYER = "Reunión ayer";
	private static final String ASUNTO_MANANA = "Reunión mañana";
	private static final ZonedDateTime FECHA_NAVIDAD = ZonedDateTime.of(2025, 12, 25, 13, 0, 0, 0, ZoneId.systemDefault());

	@Autowired
	private JacksonTester<Reunion> json;

    @Autowired
    private JacksonTester<Reunion[]> jsonList;

    private Reunion reunion;
    private Reunion[] reuniones;

    @BeforeEach
    void setUp() {
    	reunion = new Reunion(ID_NAVIDAD, ASUNTO_NAVIDAD, FECHA_NAVIDAD);
    	
        reuniones = Arrays.array(
                new Reunion(ID_NAVIDAD, ASUNTO_NAVIDAD, null),
                new Reunion(ID_AYER, ASUNTO_AYER, null),
                new Reunion(ID_MANANA, ASUNTO_MANANA, null));
    }

    @Test
    void testSerializacionReunion() throws IOException {
        assertThat(json.write(reunion)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(reunion)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(reunion)).extractingJsonPathNumberValue("@.id")
                .isEqualTo((int)ID_NAVIDAD);
        assertThat(json.write(reunion)).hasJsonPathStringValue("@.asunto");
        assertThat(json.write(reunion)).extractingJsonPathStringValue("@.asunto")
                .isEqualTo(ASUNTO_NAVIDAD);
    }

    @Test
    void testDeserializacionReunion() throws IOException {
        String expected = """
                {
                    "id": 99,
                    "asunto": "Reunión Navidad", 
                    "fecha": "2025-12-25T12:00Z"
                }
                """;
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).asunto()).isEqualTo(ASUNTO_NAVIDAD);
        assertThat(json.parseObject(expected).fecha()).isEqualTo(FECHA_NAVIDAD);
    }

    @Test
    void testSerializacionLista() throws IOException {
        assertThat(jsonList.write(reuniones)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void testDeserializacionLista() throws IOException {
        String expected = """
                [
				  {"id": 99, "asunto": "Reunión Navidad" , "fecha": null},
				  {"id": 100, "asunto": "Reunión ayer" , "fecha": null},
				  {"id": 101, "asunto": "Reunión mañana", "fecha": null}
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(reuniones);
    }
}
