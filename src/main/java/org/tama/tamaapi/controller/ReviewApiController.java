package org.tama.tamaapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.tama.sharelib.common.dto.CustomPageRequest;
import org.tama.sharelib.common.dto.CustomSort;
import org.tama.sharelib.common.dto.SortValidator;
import org.tama.tamaapi.domain.item.Review;
import org.tama.tamaapi.dto.responseDto.review.MyPageReview;
import org.tama.tamaapi.dto.responseDto.review.ReviewResponse;
import org.tama.tamaapi.repository.item.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewRepository reviewRepository;
    private final SortValidator sortValidator;

    @GetMapping("/api/reviews")
    //select 필드 너무 많아서 dto 조회 개선 필요
    public MyPageReview<ReviewResponse> reviews(@RequestParam Long colorItemId, @Valid CustomPageRequest customPageRequest, @RequestParam CustomSort sort) {

        if(!sort.getProperty().equals("createdAt"))
            throw new IllegalArgumentException("유효한 property가 아닙니다");

        sortValidator.validate(sort);

        Double avgRating = reviewRepository.findAvgRatingByColorItemId(colorItemId).orElse(0.0);

        PageRequest pageRequest = PageRequest.of(customPageRequest.getPage()-1, customPageRequest.getSize()
                , Sort.by(new Sort.Order(sort.getDirection(), sort.getProperty()), new Sort.Order(Sort.Direction.DESC, "id")));
        Page<Review> reviews = reviewRepository.findReviewsByColorItemId(colorItemId, pageRequest);

        List<ReviewResponse> reviewResponses = reviews.stream().map(ReviewResponse::new).toList();
        return new MyPageReview<>(avgRating, reviewResponses, reviews.getPageable(), reviews.getTotalPages(), reviews.getTotalElements());
    }

}
