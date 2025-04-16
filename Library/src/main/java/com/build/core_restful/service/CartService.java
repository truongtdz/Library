package com.build.core_restful.service;

import com.build.core_restful.domain.Cart;
import com.build.core_restful.domain.request.CartRequest;
import com.build.core_restful.domain.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CartService {
    PageResponse<Object> getByUser(Long userId, Pageable pageable);

    Cart addBookToCart(CartRequest cartRequest);

    boolean deleteBookAtCart(Long id);
}
