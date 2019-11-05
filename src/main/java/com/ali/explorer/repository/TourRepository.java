package com.ali.explorer.repository;

import java.util.List;

import javax.websocket.server.PathParam;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.ali.explorer.domain.Tour;

public interface TourRepository extends PagingAndSortingRepository<Tour,Integer> {
	Page<Tour> findByTourPackageCode(@PathParam("code") String code, Pageable pageable);

	@RestResource(exported = false)
	@Override
	<S extends Tour> S save(S s);

	@RestResource(exported = false)
		@Override
	<S extends Tour> Iterable<S> saveAll(Iterable<S> iterable);


	@RestResource(exported = false)
	@Override
	void deleteById(Integer integer);


	@RestResource(exported = false)
	@Override
	void delete(Tour tour);


	@RestResource(exported = false)
	@Override
	void deleteAll(Iterable<? extends Tour> iterable);


	@RestResource(exported = false)
	@Override
	void deleteAll();

	@RestResource(exported = false)
	List<Tour> save(List<Tour> tour);


	@Query("select t from Tour t where t.id=?1")
	Tour findByTourId(Integer tourId);
}
