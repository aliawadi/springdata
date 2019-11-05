package com.ali.explorer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ali.explorer.domain.TourPackage;
import com.ali.explorer.repository.TourPackageRepository;

@Service
public class TourPackageService {

	TourPackageRepository tourPackageRepository;

	@Autowired
	public TourPackageService(TourPackageRepository tourPackageRepository) {
		this.tourPackageRepository = tourPackageRepository;
	}


	public TourPackageRepository createTourPackage(String code , String name){
		if(tourPackageRepository.findByName(name)==null){
			 tourPackageRepository.save(new TourPackage(code,name));
		}
		return  null;
	}

	public Iterable<TourPackage> lookup(){
		return tourPackageRepository.findAll();
	}

	public long total(){
		return tourPackageRepository.count();
	}
}
