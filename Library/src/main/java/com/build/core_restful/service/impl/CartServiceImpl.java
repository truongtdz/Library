package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Cart;
import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.CartRequest;
import com.build.core_restful.domain.response.CartResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.CartRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.CartService;
import com.build.core_restful.util.enums.UserStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BookMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, BookRepository bookRepository, BookMapper bookMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public PageResponse<Object> getByUser(Long id, Pageable pageable) {
        if(!userRepository.existsById(id)){
            throw new NewException("User have id: " + id + " not exist!");
        }

        Page<Cart> page = cartRepository.findByUserId(id, pageable);

        Page<CartResponse> pageResponse = page.map(
            cart -> CartResponse.builder()
                                .userId(cart.getUser().getId())
                                .quantity(cart.getQuantity())
                                .rentedDay(cart.getRentedDay())
                                .book(bookMapper.toBookResponse(cart.getBook()))
                                .build() 
        );

        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    @Transactional
    public boolean updateCartByUser(CartRequest cartRequest) {
        User user = userRepository.findByIdAndStatus(cartRequest.getUserId(), UserStatusEnum.Active.toString())
                                .orElseThrow(() -> new NewException("User with id: " + cartRequest.getUserId() + " not found"));

        List<Cart> newCarts = new ArrayList<>();
        for(CartRequest.BookReq cart : cartRequest.getBooks()){
            Book book = bookRepository.findById(cart.getBookId())
                    .orElseThrow(() -> new NewException("Book with id: " + cart.getBookId() + " not found"));

            Cart newCart = Cart.builder()
                    .book(book)
                    .user(user)
                    .rentedDay(cart.getRentedDay())
                    .quantity(cart.getQuantity())
                    .build();

            newCarts.add(newCart);
        }

        try {
            cartRepository.deleteByUserId(cartRequest.getUserId());
            cartRepository.saveAll(newCarts);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
