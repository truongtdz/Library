package com.build.core_restful.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.response.BookRecommendResponse;

@Service
public interface TrainService {
    void trainDataEveryDay();

    List<BookRecommendResponse> predictCategory(Long age, String gender, String city);
}
