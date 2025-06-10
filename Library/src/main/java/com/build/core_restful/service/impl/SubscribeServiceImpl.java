package com.build.core_restful.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.build.core_restful.domain.Subscribe;
import com.build.core_restful.domain.request.SubscribeRequest;
import com.build.core_restful.domain.response.BookRecommendResponse;
import com.build.core_restful.repository.SubscribeRepository;
import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.service.TrainService;
import com.build.core_restful.system.EmailUtil;
import com.build.core_restful.util.exception.NewException;

@Service
public class SubscribeServiceImpl implements SubscribeService{
    private final SubscribeRepository subscribeRepository;
    private final EmailUtil emailUtil;
    private final TrainService trainService;

    public SubscribeServiceImpl (
        SubscribeRepository subscribeRepository,
        EmailUtil emailUtil,
        TrainService trainService
    ){
        this.subscribeRepository = subscribeRepository;
        this.emailUtil = emailUtil;
        this.trainService = trainService;
    };

    @Override
    public boolean existsByEmail(String email) {
        return subscribeRepository.existsByEmail(email);
    }

    @Override
    public boolean createSubscribe(SubscribeRequest request) {
        try {
            subscribeRepository.save(
                Subscribe.builder()
                .age(request.getAge())
                .email(request.getEmail())
                .fullName(request.getFullName())                    
                .build()
            );
            return true;
        } catch (Exception e) {
            throw new NewException("Subscribe not success");
        }
    }

    @Override
    public boolean deleteSubscribe(String email) {
        try {
            subscribeRepository.deleteByEmail(email);
            return true;
        } catch (Exception e) {
            throw new NewException("Delete fail");
        }
    }

    @Override
    @Transactional
    public void sendEmailRecommendBook() {
        List<Subscribe> subscribes = subscribeRepository.findAll();

        for(Subscribe subscribe: subscribes){
            String city = Optional.ofNullable(subscribe.getCity())
                      .orElse(null);

            List<BookRecommendResponse> books = trainService.predictCategory(subscribe.getAge(), subscribe.getGender(), city);
            emailUtil.sendEmailRecommendBook(subscribe.getEmail(), subscribe.getFullName(), books);
        }
    }


    
}
