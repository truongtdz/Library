package com.build.core_restful.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Subscribe;
import com.build.core_restful.domain.request.SubscribeRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.SubscribeRepository;
import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BookMapper;
import com.build.core_restful.util.system.EmailUtil;

@Service
public class SubscribeServiceImpl implements SubscribeService{
    private final SubscribeRepository subscribeRepository;
    private final BookRepository bookRepository;
    private final EmailUtil emailUtil;
    private final BookMapper bookMapper;

    public SubscribeServiceImpl (
        SubscribeRepository subscribeRepository,
        BookRepository bookRepository,
        BookMapper bookMapper,
        EmailUtil emailUtil
    ){
        this.subscribeRepository = subscribeRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.emailUtil = emailUtil;
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

    @Override
    @Transactional
    public void sendEmailToUser() {
        List<Subscribe> subscribes = subscribeRepository.findAll();

        List<Book> topBooks = bookRepository.findTop10ByOrderByQuantityRentedDesc();

        List<BookResponse> responseList = topBooks.stream()
        .map(bookMapper::toBookResponse)
        .collect(Collectors.toList());

        for(Subscribe subscribe: subscribes){
            emailUtil.sendEmailTemplate(subscribe.getEmail(), subscribe.getFullName(), responseList);
        }
    }
    
}
