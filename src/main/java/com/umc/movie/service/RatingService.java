package com.umc.movie.service;

import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.entity.Rating;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RatingService {

 private final RatingRepository ratings;
 private final MemberRepository members;

 public RatingService(
         RatingRepository ratings,
         MemberRepository members
 ) {
  this.ratings = ratings;
  this.members = members;
 }

 // 기존 회원별 평점 조회
 public List<RatingResponse> getRatingsByMember(Long memberId) {
  if (!members.existsById(memberId)) {
   throw new ResponseStatusException(
           HttpStatus.NOT_FOUND,
           "존재하지 않는 유저입니다."
   );
  }

  return ratings.findByMember_MemberIdOrderByRatingIdDesc(memberId)
          .stream()
          .map(RatingResponse::from)
          .toList();
 }

 // 평점 등록
 @Transactional
 public Long create(Long memberId, CreateRatingRequest request) {
  Member member = members.findById(memberId)
          .orElseThrow(() -> new ResponseStatusException(
                  HttpStatus.NOT_FOUND,
                  "존재하지 않는 유저입니다."
          ));

  if (ratings.existsByMember_MemberIdAndMovieId(
          memberId, request.movieId()
  )) {
   throw new ResponseStatusException(
           HttpStatus.CONFLICT,
           "이미 이 영화에 평점을 남기셨습니다."
   );
  }

  Rating rating = new Rating(
          member,
          request.movieId(),
          request.score(),
          request.comment()
  );

  return ratings.save(rating).getRatingId();
 }

 // 평점 수정
 @Transactional
 public void update(
         Long ratingId,
         Long memberId,
         UpdateRatingRequest request
 ) {
  Rating rating = findOwnedRating(ratingId, memberId);

  rating.update(request.score(), request.comment());
 }

 // 평점 삭제
 @Transactional
 public void delete(Long ratingId, Long memberId) {
  Rating rating = findOwnedRating(ratingId, memberId);

  ratings.delete(rating);
 }

 // 평점 존재 여부와 작성자 확인
 private Rating findOwnedRating(Long ratingId, Long memberId) {
  Rating rating = ratings.findById(ratingId)
          .orElseThrow(() -> new ResponseStatusException(
                  HttpStatus.NOT_FOUND,
                  "존재하지 않는 평점입니다."
          ));

  if (!rating.getMember().getMemberId().equals(memberId)) {
   throw new ResponseStatusException(
           HttpStatus.FORBIDDEN,
           "본인이 작성한 평점만 수정하거나 삭제할 수 있습니다."
   );
  }

  return rating;
 }
}
