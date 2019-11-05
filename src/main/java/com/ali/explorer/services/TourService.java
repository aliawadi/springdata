package com.ali.explorer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ali.explorer.domain.Difficulty;
import com.ali.explorer.domain.Region;
import com.ali.explorer.domain.Tour;
import com.ali.explorer.domain.TourPackage;
import com.ali.explorer.repository.TourPackageRepository;
import com.ali.explorer.repository.TourRepository;

@Service
public class TourService {

	TourPackageRepository tourPackageRepository;
	TourRepository tourRepository;


	@Autowired
	public TourService(TourPackageRepository tourPackageRepository, TourRepository tourRepository) {
		this.tourPackageRepository = tourPackageRepository;
		this.tourRepository = tourRepository;
	}

	public Tour createTour(String title, String description, String blurb, Integer price, String duration, String bullets,
			String keywords,String tourPackageName, Difficulty difficulty, Region region){
		TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName);
		if(null==tourPackage){
			throw  new RuntimeException("Cant find the Tour POackage with :"+ tourPackageName);
		}
		return tourRepository.save( new Tour( title,  description,  blurb,  price,  duration,  bullets,
				 keywords, tourPackage,  difficulty,  region));


	}

	public Iterable<Tour> createTour1(List<Tour> tours){

		return tourRepository.save(tours);



	}


	public Iterable<Tour> lookup(){
		return tourRepository.findAll();
	}

	public long total(){
		return tourRepository.count();
	}
}
