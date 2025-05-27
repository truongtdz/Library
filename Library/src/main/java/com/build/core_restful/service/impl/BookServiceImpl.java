package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Category;
import com.build.core_restful.domain.Image;
import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.CategoryRepository;
import com.build.core_restful.repository.AuthorRepository;
import com.build.core_restful.repository.specification.BookSpecification;
import com.build.core_restful.service.BookService;
import com.build.core_restful.util.enums.TypeQuantityBook;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BookMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorsRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(
                            BookRepository bookRepository, 
                            CategoryRepository categoryRepository, 
                            AuthorRepository authorsRepository, 
                            BookMapper bookMapper
    ) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.authorsRepository = authorsRepository;
        this.bookMapper = bookMapper;

    }

    @Override
    public PageResponse<Object> getAllBooks(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(pageable);
        Page<BookResponse> pageResponse = page.map(bookMapper::toBookResponse);

        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NewException("Book with id: " + id + " not found")); 
            
        book.setQuantityView(book.getQuantityView() + 1);

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new NewException("Category id " + bookRequest.getCategoryId() + " not found"));
        
        Author author = authorsRepository.findById(bookRequest.getAuthorsId())
                .orElseThrow(() -> new NewException("Author id " + bookRequest.getAuthorsId() + " not found"));

        Book book = bookMapper.toBook(bookRequest);
        book.setCategory(category);
        book.setAuthor(author);

        if (bookRequest.getImageList() != null) {
            List<Image> images = bookRequest.getImageList().stream()
                    .map(imageReq -> Image.builder()
                            .url(imageReq.getUrl())
                            .isDefault(imageReq.getIsDefault())
                            .book(book) 
                            .build())
                    .collect(Collectors.toList());
            book.setImages(images); 
        }

        return bookMapper.toBookResponse(bookRepository.save(book));
    }


    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NewException("Book with id: " + id + " not found"));

        bookMapper.updateBook(book, bookRequest);

        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new NewException("Category id " + bookRequest.getCategoryId() + " not found"));
        book.setCategory(category);

        Author author = authorsRepository.findById(bookRequest.getAuthorsId())
                .orElseThrow(() -> new NewException("Author id " + bookRequest.getAuthorsId() + " not found"));
        book.setAuthor(author);

        if (bookRequest.getImageList() != null) {
            // book.getImages().clear();

            List<Image> newImages = bookRequest.getImageList().stream()
                    .map(imageReq -> Image.builder()
                            .url(imageReq.getUrl())
                            .isDefault(imageReq.getIsDefault())
                            .book(book)
                            .build())
                    .collect(Collectors.toList());

            book.getImages().addAll(newImages);
        }


        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    public boolean deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NewException("Book with id: " + id + " not found");
        }
        bookRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BookResponse> getTop10BookBy(String getBookBy) {
        return switch (getBookBy) {
            case "sell" -> bookRepository.findTop10ByOrderByQuantitySellDesc().stream()
                    .map(bookMapper::toBookResponse)
                    .collect(Collectors.toList());
            case "view" -> bookRepository.findTop10ByOrderByQuantityViewDesc().stream()
                    .map(bookMapper::toBookResponse)
                    .collect(Collectors.toList());
            case "like" -> bookRepository.findTop10ByOrderByQuantityLikeDesc().stream()
                    .map(bookMapper::toBookResponse)
                    .collect(Collectors.toList());
            default -> null;
        };
    };

    @Override
    public Integer getQuantityBook(TypeQuantityBook quantityBook) {
        return switch (quantityBook.toString()) {
            case "All" -> bookRepository.getTotalBooks();
            case "Retailing" -> bookRepository.getQuantityBookLated();
            case "Lated" -> bookRepository.getQuantityBookLated();
            default -> null;
        };
    };

    @Override
    public SearchResponse searchBook(
            String keyword,
            Long categoryId,
            Long authorId,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        Specification<Book> spec = BookSpecification.filterBooks(
                keyword,
                categoryId,
                authorId,
                language,
                minPrice,
                maxPrice
        );

        Page<Book> page = bookRepository.findAll(spec, pageable);
        Page<BookResponse> pageResponse = page.map(bookMapper::toBookResponse);

        return SearchResponse.builder()
                .keyword(keyword)
                .result(PageResponse.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .content(pageResponse.getContent())
                        .build())
                .build();
    };
}
