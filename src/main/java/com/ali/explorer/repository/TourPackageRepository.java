package com.ali.explorer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.ali.explorer.domain.TourPackage;

@RepositoryRestResource(collectionResourceRel ="packages", path="packages")
public interface TourPackageRepository extends CrudRepository<TourPackage,String> {


	TourPackage findByName(@Param("name") String name);

	//@RestResource won’t expose those HTTP methods on those resources.
	@RestResource(exported = false)
	@Override
	<S extends TourPackage> S save(S s);

	@RestResource(exported = false)
	@Override
	<S extends TourPackage> Iterable<S> saveAll(Iterable<S> iterable);


	@RestResource(exported = false)
	@Override
	void deleteById(String s);

	@RestResource(exported = false)
	@Override
	void delete(TourPackage tourPackage);



	@RestResource(exported = false)
	@Override
	void deleteAll(Iterable<? extends TourPackage> iterable);

	@RestResource(exported = false)
	@Override
	void deleteAll();
//TourPackage findOne(String tourPackageCode);
}
