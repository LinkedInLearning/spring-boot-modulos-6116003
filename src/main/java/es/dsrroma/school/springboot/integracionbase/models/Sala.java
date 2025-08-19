package es.dsrroma.school.springboot.integracionbase.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sala {
    @Id
    @Column(length = 7)
    private String id;

    private String descripcion;
    private int capacidad;
}
