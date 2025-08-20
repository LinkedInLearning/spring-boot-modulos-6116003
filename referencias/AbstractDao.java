package com.linkedin.learning.otrareunionmas.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.linkedin.learning.otrareunionmas.utiles.EntityManagerUtil;

public abstract class AbstractDao<T> implements Dao<T> {

	private EntityManager entityManager = EntityManagerUtil.getEntityManager();
	private Class<T> clazz;

	@Override
	public Optional<T> get(long id) {
		return Optional.ofNullable(entityManager.find(clazz, id));
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public List<T> getAll() {
		String qlString = "FROM " + clazz.getName();
		Query query = entityManager.createQuery(qlString);
		return query.getResultList();
	}

	@Override
	public void save(T t) {
		executeInsideTransaction(entityManager -> entityManager.persist(t));
	}

	@Override
	public void update(T t) {
		executeInsideTransaction(entityManager -> entityManager.merge(t));
	}

	@Override
	public void delete(T t) {
		executeInsideTransaction(entityManager -> entityManager.remove(t));
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	private void executeInsideTransaction(Consumer<EntityManager> action) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			action.accept(entityManager);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
	}
}
