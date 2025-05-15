package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Cart;
import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.CartRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.CartRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.CartService;
import com.build.core_restful.util.enums.UserStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BookMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        List<BookResponse> bookResponses = page.getContent().stream().map(cart -> {
            BookResponse response = bookMapper.toBookResponse(cart.getBook());

            List<BookResponse.ImageResponse> imageList = cart.getBook().getImages().stream()
                    .map(image -> BookResponse.ImageResponse.builder()
                            .isCover(image.isCover())
                            .url(image.getUrl())
                            .build())
                    .collect(Collectors.toList());

            response.setImageList(imageList);
            return response;
        }).collect(Collectors.toList());

        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(bookResponses)
                .build();
    }

    @Override
    public boolean addBookToCart(CartRequest cartRequest) {
        User user = userRepository.findByIdAndStatus(cartRequest.getUserId(), UserStatusEnum.Active);

        if(user == null){
            throw new NewException("User with id: " + cartRequest.getUserId() + " not found");
        }

        if(cartRepository.existsByUserIdAndBookId(cartRequest.getUserId(), cartRequest.getBookId())){
            throw new NewException("Book existed at my cart");
        }

        Book book = bookRepository.findById(cartRequest.getBookId())
                .orElseThrow(() -> new NewException("Book with id: " + cartRequest.getBookId() + " not found"));

        Cart newCart = Cart.builder()
                .book(book)
                .user(user)
                .build();
        try {
            cartRepository.save(newCart);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteBookAtCart(CartRequest cartRequest) {
        Cart cart = cartRepository.findByUserIdAndBookId(cartRequest.getUserId(), cartRequest.getBookId());
        try{
            cartRepository.deleteById(cart.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
