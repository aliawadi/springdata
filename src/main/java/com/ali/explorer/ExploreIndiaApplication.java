package com.ali.explorer;

import com.ali.explorer.domain.Difficulty;
import com.ali.explorer.domain.Region;
import com.ali.explorer.services.TourPackageService;
import com.ali.explorer.services.TourService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

import static com.ali.explorer.ExploreIndiaApplication.TourFromFile.importTours;

@SpringBootApplication
public class ExploreIndiaApplication implements CommandLineRunner {

	@Autowired
	TourPackageService tourPackageService;
	@Autowired
	TourService tourService;

	public static void main(String[] args) {
		SpringApplication.run(ExploreIndiaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		tourPackageService.createTourPackage("BC", "Backpack Cal");
		tourPackageService.createTourPackage("CC", "California Calm");
		tourPackageService.createTourPackage("CH", "California Hot springs");
		tourPackageService.createTourPackage("CY", "Cycle California");
		tourPackageService.createTourPackage("DS", "From Desert to Sea");
		tourPackageService.createTourPackage("KC", "Kids California");
		tourPackageService.createTourPackage("NW", "Nature Watch");
		tourPackageService.createTourPackage("SC", "Snowboard Cali");
		tourPackageService.createTourPackage("TC", "Taste of California");
		System.out.println("Number of tours packages =" + tourPackageService.total());
		tourPackageService.lookup().forEach(tourPackage -> System.out.println(tourPackage));

		//Persist the Tours to the database
		importTours().forEach(t-> tourService.createTour(
				t.title,
				t.description,
				t.blurb,
				Integer.parseInt(t.price),
				t.length,
				t.bullets,
				t.keywords,
				t.packageType,
				Difficulty.valueOf(t.difficulty),
				Region.findByLabel(t.region)));
		System.out.println("Number of tours =" + tourService.total());


	}

	/**
	 * Helper class to import the records from the ExploreCalifornia.json
	 */
	static class TourFromFile {
		//attributes as listed in the .json file
		private String packageType, title, description, blurb, price, length, bullets, keywords,  difficulty, region;

		/**
		 * Open the ExploreCalifornia.json, unmarshal every entry into a TourFromFile Object.
		 *
		 * @return a List of TourFromFile objects.
		 * @throws IOException if ObjectMapper unable to open file.
		 */
		static List<TourFromFile> importTours() throws IOException {
			return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).
					readValue(TourFromFile.class.getResourceAsStream("/json/ExploreCalifornia.json"),new TypeReference<List<TourFromFile>>(){});
		}
	}


	/*@Bean
	CommandLineRunner runner(TourService tourService) {
		return args -> {
			// read json and write to db
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Tour>> typeReference = new TypeReference<List<Tour>>(){};
			int check =  TypeReference.class.getResourceAsStream("/json/ExploreCalifornia.json").available();
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/ExploreCalifornia.json");
			try {
				List<Tour> tours = mapper.readValue(inputStream,typeReference);
				tourService.createTour1(tours);
				System.out.println("Users Saved!");
			} catch (IOException e){
				System.out.println("Unable to save users: " + e.getMessage());
			}
			System.out.println("Number of tours =" + tourService.total());
		};
	}*/
}
