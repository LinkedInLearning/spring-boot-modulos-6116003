package es.dsrroma.school.springboot.integracionbase.models;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;

public record Reunion(@Id Long id, String asunto, ZonedDateTime fecha) {

}
