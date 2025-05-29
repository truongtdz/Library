package com.build.core_restful.service.impl;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Subscribe;
import com.build.core_restful.domain.request.SubscribeRequest;
import com.build.core_restful.repository.SubscribeRepository;
import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.util.exception.NewException;

@Service
public class SubscribeServiceImpl implements SubscribeService{
    private final SubscribeRepository subscribeRepository;

    public SubscribeServiceImpl (
        SubscribeRepository subscribeRepository
    ){
        this.subscribeRepository = subscribeRepository;
    };

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
    
}
