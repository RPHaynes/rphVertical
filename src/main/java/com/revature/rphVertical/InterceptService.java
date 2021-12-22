package com.revature.rphVertical;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InterceptService<T,ID> {
	SimpleJpaRepository<T,ID> repo;
	EntityManager entityManager;
	Class<?> clazz;

	public void settClass(Class<T> tClass) {
		clazz = tClass;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setRepo(SimpleJpaRepository<T, ID> repo) {
		this.repo = repo;
	}


	public T customSelectOne(Map<String,Object> fields) {
		StringBuilder qString = new StringBuilder("SELECT"+" _"+clazz.getSimpleName()+" FROM "+clazz.getSimpleName()+" _"+clazz.getSimpleName());
		Set<String> joinSet = new HashSet<>();
		String[] keys = fields.keySet().toArray(new String[0]);
		for(String key: keys){
			String[] parts = key.split("\\.");
			for (int j = 0; j < parts.length-1; j++) {
				if (j == 0) joinSet.add(" JOIN"+" _"+clazz.getSimpleName()+"."+parts[j]+" _"+parts[j]);
				else joinSet.add(" JOIN"+" _"+parts[j-1]+"."+parts[j]+" _"+parts[j]);
			}
		}
		System.out.println(joinSet);
		for (String s:joinSet) {qString.append(s);}
		joinSet = new HashSet<>();
		joinSet.add(" WHERE");
		int i =0;
		for(String key: keys){
			String[] parts = key.split("\\.");
			if (parts.length == 1) {
				if (joinSet.add(" _" + clazz.getSimpleName() + "." + parts[0] + " = ?" + i)) i++;
			}
			else
			if (joinSet.add(" _" + parts[parts.length-2] + "." + parts[parts.length-1] + " = ?" + i)) i++;
		}
		for (String s:joinSet) {qString.append(s);}
		System.out.println(qString);
		Query query = entityManager.createQuery(qString.toString());
		for (int j = 0; j < i; j++) {
			query.setParameter(j,fields.get(keys[j]));
		}
		System.out.println(qString);

		List l = query.getResultList();

		return  (l.size() == 0)?null: (T) l.get(0);
	}
	public List<T> customSelectAll(Map<String,Object> fields) {
		StringBuilder qString = new StringBuilder("SELECT"+" _"+clazz.getSimpleName()+" FROM "+clazz.getSimpleName()+" _"+clazz.getSimpleName());
		Set<String> joinSet = new HashSet<>();
		String[] keys = fields.keySet().toArray(new String[0]);
		for(String key: keys){
			String[] parts = key.split("\\.");
			for (int j = 0; j < parts.length-1; j++) {
				if (j == 0) joinSet.add(" JOIN"+" _"+clazz.getSimpleName()+"."+parts[j]+" _"+parts[j]);
				else joinSet.add(" JOIN"+" _"+parts[j-1]+"."+parts[j]+" _"+parts[j]);
			}
		}
		System.out.println(joinSet);
		for (String s:joinSet) {qString.append(s);}
		joinSet = new HashSet<>();
		joinSet.add(" WHERE");
		int i =0;
		for(String key: keys){
			String[] parts = key.split("\\.");
			if (parts.length == 1) {
				if (joinSet.add(" _" + clazz.getSimpleName() + "." + parts[0] + " = ?" + i)) i++;
			}
			else
				if (joinSet.add(" _" + parts[parts.length-2] + "." + parts[parts.length-1] + " = ?" + i)) i++;
		}
		for (String s:joinSet) {qString.append(s);}
		System.out.println(qString);
		Query query = entityManager.createQuery(qString.toString());
		for (int j = 0; j < i; j++) {
			query.setParameter(j,fields.get(keys[j]));
		}
		System.out.println(qString);
		return  (List<T>) query.getResultList();
	}


	//DELETES
	public void deleteById(ID id) {
		entityManager.getTransaction().begin();
		repo.deleteById(id);
		entityManager.getTransaction().commit();
	} //

	public void deleteAllById(Iterable<? extends ID> ids) {
		entityManager.getTransaction().begin();
		repo.deleteAllById(ids);
		entityManager.getTransaction().commit();
	}
	public void deleteAllByIdInBatch(Iterable<ID> ids) {
		entityManager.getTransaction().begin();
		repo.deleteAllByIdInBatch(ids);
		entityManager.getTransaction().commit();
	}

	public void delete(T entity) {
		entityManager.getTransaction().begin();
		repo.delete(entity);
		entityManager.getTransaction().commit();
	}


	public void deleteAll(Iterable<? extends T> entities) {
		entityManager.getTransaction().begin();
		repo.deleteAll(entities);
		entityManager.getTransaction().commit();
	}
	public void deleteAllInBatch(Iterable<T> entities) {
		entityManager.getTransaction().begin();
		repo.deleteAllInBatch(entities);
		entityManager.getTransaction().commit();
	}

	public void deleteAll() {
		entityManager.getTransaction().begin();
		repo.deleteAll();
		entityManager.getTransaction().commit();
	}
	public void deleteAllInBatch() {
		entityManager.getTransaction().begin();
		repo.deleteAllInBatch();
		entityManager.getTransaction().commit();
	}

	//FINDS

	public T getById(ID id) {
		entityManager.getTransaction().begin();
		T t = repo.getById(id);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
		entityManager.getTransaction().begin();
		R t = repo.findBy(example,queryFunction);
		entityManager.getTransaction().commit();
		return t;
	}

	public Optional<T> findById(ID id) {
		entityManager.getTransaction().begin();
		Optional<T> t = repo.findById(id);
		entityManager.getTransaction().commit();
		return t;
	}
	public Optional<T> findOne(@Nullable Specification<T> spec) {
		entityManager.getTransaction().begin();
		Optional<T>  t = repo.findOne(spec);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> Optional<S> findOne(Example<S> example) {
		entityManager.getTransaction().begin();
		Optional<S> t = repo.findOne(example);
		entityManager.getTransaction().commit();
		return t;
	}

	public List<T> findAll() {
		entityManager.getTransaction().begin();
		List<T> t = repo.findAll();
		entityManager.getTransaction().commit();
		return t;
	}
	public List<T> findAllById(Iterable<ID> ids) {
		entityManager.getTransaction().begin();
		List<T> t = repo.findAllById(ids);
		entityManager.getTransaction().commit();
		return t;
	}
	public List<T> findAll(Sort sort) {
		entityManager.getTransaction().begin();
		List<T> t = repo.findAll(sort);
		entityManager.getTransaction().commit();
		return t;
	}
	public List<T> findAll(@Nullable Specification<T> spec) {
		entityManager.getTransaction().begin();
		List<T> t = repo.findAll(spec);
		entityManager.getTransaction().commit();
		return t;
	}
	public List<T> findAll(@Nullable Specification<T> spec, Sort sort) {
		entityManager.getTransaction().begin();
		List<T> t = repo.findAll(spec,sort);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> List<S> findAll(Example<S> example) {
		entityManager.getTransaction().begin();
		List<S> t = repo.findAll(example);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		entityManager.getTransaction().begin();
		List<S> t= repo.findAll(example,sort);
		entityManager.getTransaction().commit();
		return t;
	}

	public Page<T> findAll(Pageable pageable) {
		entityManager.getTransaction().begin();
		Page<T> t = repo.findAll(pageable);
		entityManager.getTransaction().commit();
		return t;
	}
	public Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable) {
		entityManager.getTransaction().begin();
		Page<T> t = repo.findAll(spec,pageable);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		entityManager.getTransaction().begin();
		Page<S> t = repo.findAll(example,pageable);
		entityManager.getTransaction().commit();
		return t;
	}


	//MISC
	public boolean existsById(ID id) {
		entityManager.getTransaction().begin();
		boolean t = repo.existsById(id);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> long count(Example<S> example) {
		entityManager.getTransaction().begin();
		long t = repo.count(example);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> boolean exists(Example<S> example) {
		entityManager.getTransaction().begin();
		boolean t =  repo.exists(example);
		entityManager.getTransaction().commit();
		return t;
	}
	public long count() {
		entityManager.getTransaction().begin();
		long t = repo.count();
		entityManager.getTransaction().commit();
		return t;
	}
	public long count(@Nullable Specification<T> spec) {
		entityManager.getTransaction().begin();
		long t = repo.count(spec);
		entityManager.getTransaction().commit();
		return t;
	}
	public void flush() {
		entityManager.getTransaction().begin();
		repo.flush();
		entityManager.getTransaction().commit();
	}

	//SAVES
	public <S extends T> S save(S entity) {

		entityManager.getTransaction().begin();
		S s = repo.save(entity);
		entityManager.getTransaction().commit();
		return s;
	}
	public <S extends T> S saveAndFlush(S entity) {
		entityManager.getTransaction().begin();
		S t = repo.saveAndFlush(entity);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		entityManager.getTransaction().begin();
		List<S> t = repo.saveAll(entities);
		entityManager.getTransaction().commit();
		return t;
	}
	public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
		entityManager.getTransaction().begin();
		List<S> t = repo.saveAllAndFlush(entities);
		entityManager.getTransaction().commit();
		return t;
	}



}
