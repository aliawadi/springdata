package com.ali.explorer.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import com.ali.explorer.domain.Tour;
import com.ali.explorer.domain.TourRating;
import com.ali.explorer.domain.TourRatingPk;
import com.ali.explorer.repository.TourRatingRepository;
import com.ali.explorer.repository.TourRepository;

/**
 * Tour Rating Controller
 *
 * Created by Mary Ellen Bowman
 */
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
    TourRatingRepository tourRatingRepository;
    TourRepository tourRepository;

    @Autowired
    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    protected TourRatingController() {

    }


    /**
     * Convert the TourRating entity to a RatingDto
     *
     * @param tourRating
     * @return RatingDto
     */
    private RatingDto toDto(TourRating tourRating) {
        return new RatingDto(tourRating.getScore(), tourRating.getComment(), tourRating.getPk().getCustomerId());
    }

    /**
     * Verify and return the TourRating for a particular tourId and Customer
     * @param tourId
     * @param customerId
     * @return the found TourRating
     * @throws NoSuchElementException if no TourRating found
     */
    private TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException {
        TourRating rating = tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId);
        if (rating == null) {
            throw new NoSuchElementException("Tour-Rating pair for request("
                    + tourId + " for customer" + customerId);
        }
        return rating;
    }

    /**
     * Verify and return the Tour given a tourId.
     *
     * @param tourId
     * @return the found Tour
     * @throws NoSuchElementException if no Tour found.
     */
    private Tour verifyTour(Integer tourId) throws NoSuchElementException {
       Tour tour = tourRepository.findByTourId( tourId);
        if (tour== null) {
            throw new NoSuchElementException("Tour does not exist " + tourId);
        }
        return tour ;
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     *
     * @param ex
     * @return Error message String.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();

    }


    @RequestMapping(method = RequestMethod.POST,produces = "application/xml")
      @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") Integer tourId ,@RequestBody @Validated RatingDto ratingDto){
        Tour tour =  verifyTour(tourId);
        this.tourRatingRepository.save(new TourRating(new TourRatingPk(tour,ratingDto.getCustomerId()),ratingDto.getScore(),ratingDto.getComment()));

    }


    @RequestMapping(method = RequestMethod.GET)
    public Page<RatingDto> getAllRatingForTour(@PathVariable(value = "tourId") Integer tourId,Pageable pageable){
        verifyTour(tourId);
        Page<TourRating> tourRatingPage= tourRatingRepository.findByPkTourId(tourId,pageable);
        List<RatingDto> ratingDtoList=tourRatingPage.getContent().stream().map(tourRating -> toDto(tourRating)).collect(Collectors.toList());
        return new PageImpl<RatingDto>(ratingDtoList,pageable,tourRatingPage.getTotalPages());
    }

    @RequestMapping(method = RequestMethod.GET,path = "/average")
    public AbstractMap.SimpleEntry<String,Double> getAverage(@PathVariable(value = "tourId") Integer tourId) {
        verifyTour(tourId);
        List<TourRating> tourRatings = this.tourRatingRepository.findByPkTourId(tourId);
        OptionalDouble average= tourRatings.stream().mapToInt(TourRating::getScore).average();
        return  new AbstractMap.SimpleEntry<String, Double>("average",average.isPresent()?average.getAsDouble():null);

    }

    @RequestMapping(method = RequestMethod.PUT)
    public RatingDto updateTourRatingWithPut(@PathVariable(value = "tourId") Integer tourId,@RequestBody @Validated RatingDto ratingDto){

        TourRating tourRating=verifyTourRating(tourId,ratingDto.getCustomerId());
        tourRating.setComment(ratingDto.getComment());
        tourRating.setScore(ratingDto.getScore());
        return toDto(this.tourRatingRepository.save(tourRating));
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public RatingDto updateTourRatingWithPatch(@PathVariable(value = "tourId") Integer tourId,@RequestBody @Validated RatingDto ratingDto){

        TourRating tourRating=verifyTourRating(tourId,ratingDto.getCustomerId());
        if(null!=ratingDto.getComment()) {
            tourRating.setComment(ratingDto.getComment());
        }
        if(null!=ratingDto.getScore()) {
            tourRating.setScore(ratingDto.getScore());
        }
        return toDto(this.tourRatingRepository.save(tourRating));
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/{customerId}")
    public void deleteTourRating(@PathVariable(value = "tourId") Integer tourId,@PathVariable(value = "customerId") Integer customerId){
        TourRating tourRating=verifyTourRating(tourId,customerId);
        this.tourRatingRepository.delete(tourRating);
    }
}
