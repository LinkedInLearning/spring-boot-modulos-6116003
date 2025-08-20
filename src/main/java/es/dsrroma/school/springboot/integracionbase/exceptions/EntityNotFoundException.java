package es.dsrroma.school.springboot.integracionbase.exceptions;

public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String entityName, Object id) {
		super(entityName + " con ID " + id + " no encontrada.");
	}
}
