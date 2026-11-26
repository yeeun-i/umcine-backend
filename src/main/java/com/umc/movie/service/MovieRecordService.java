package com.umc.movie.service;

import com.umc.movie.dto.CreateFavoriteRequest;
import com.umc.movie.dto.CreateMovieRecordRequest;
import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.FavoriteResponse;
import com.umc.movie.dto.MovieRecordResponse;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieRecordService {

    private final RatingService ratingService;
    private final FavoriteService favoriteService;
    private final RatingRepository ratingRepository;

    public MovieRecordService(
            RatingService ratingService,
            FavoriteService favoriteService,
            RatingRepository ratingRepository
    ) {
        this.ratingService = ratingService;
        this.favoriteService = favoriteService;
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public MovieRecordResponse create(
            Long memberId, CreateMovieRecordRequest request
    ) {
        Long ratingId = ratingService.create(
                memberId,
                new CreateRatingRequest(
                        request.movieId(), request.score(), request.comment()
                )
        );

        FavoriteResponse favorite = null;
        if (request.addToFavorites()) {
            favorite = favoriteService.create(
                    memberId,
                    new CreateFavoriteRequest(request.movieId())
            );
        }

        RatingResponse rating = ratingRepository.findById(ratingId)
                .map(RatingResponse::from)
                .orElseThrow();

        return new MovieRecordResponse(rating, favorite);
    }
}