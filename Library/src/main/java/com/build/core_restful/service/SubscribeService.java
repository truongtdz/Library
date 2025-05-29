package com.build.core_restful.service;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.request.SubscribeRequest;

@Service
public interface SubscribeService {
    boolean createSubscribe(SubscribeRequest request);

    boolean deleteSubscribe(String email);

    void sendEmailToUser();
}
