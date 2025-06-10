package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Category;
import com.build.core_restful.domain.Image;
import com.build.core_restful.domain.RentalItem;
import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.CategoryRepository;
import com.build.core_restful.repository.RentalItemRepository;
import com.build.core_restful.repository.AuthorRepository;
import com.build.core_restful.repository.specification.BookSpecification;
import com.build.core_restful.service.BookService;
import com.build.core_restful.util.enums.EntityStatusEnum;
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
    private final RentalItemRepository rentalItemRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(
        BookRepository bookRepository, 
        CategoryRepository categoryRepository, 
        AuthorRepository authorsRepository, 
        BookMapper bookMapper,
        RentalItemRepository rentalItemRepository
    ) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.authorsRepository = authorsRepository;
        this.bookMapper = bookMapper;
        this.rentalItemRepository = rentalItemRepository;
    };

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                .orElseThrow(() -> new NewException("Book with id: " + id + " not found")); 
            
        book.setQuantityViewed(book.getQuantityViewed() + 1);

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

        book.setStatus(EntityStatusEnum.Active.toString());
        book.setTypeActive("CREATE");

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
        Book book = bookRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                .orElseThrow(() -> new NewException("Book with id: " + id + " not found"));

        bookMapper.updateBook(book, bookRequest);

        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new NewException("Category id " + bookRequest.getCategoryId() + " not found"));
        book.setCategory(category);

        Author author = authorsRepository.findById(bookRequest.getAuthorsId())
                .orElseThrow(() -> new NewException("Author id " + bookRequest.getAuthorsId() + " not found"));
        book.setAuthor(author);

        if (bookRequest.getImageList() != null) {
            List<Image> newImages = bookRequest.getImageList().stream()
                    .map(imageReq -> Image.builder()
                            .url(imageReq.getUrl())
                            .isDefault(imageReq.getIsDefault())
                            .book(book)
                            .build())
                    .collect(Collectors.toList());

            book.getImages().clear();  
            book.getImages().addAll(newImages);  
        }

        book.setTypeActive("UPDATE");

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    public boolean softDeleteBooks(List<Long> booksId) {
        try {
            for(Long id : booksId){
                Book book = bookRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Book with id: " + id + " not found"));
                    
                book.setStatus(EntityStatusEnum.Delete.toString());

                book.setTypeActive("DELETE");

                bookRepository.save(book);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error soft deleting books: " + e.getMessage());
        }
    }

    @Override
    public boolean restoreBooks(List<Long> booksId) {
        try {
            for(Long id : booksId){
                Book book = bookRepository.findByIdAndStatus(id, EntityStatusEnum.Delete.toString())
                    .orElseThrow(() -> new NewException("Book with id: " + id + " not found"));
                    
                book.setStatus(EntityStatusEnum.Active.toString());

                book.setTypeActive("RESTORE");

                bookRepository.save(book);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error restoring books: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteBook(Long id){
        try {
            List<RentalItem> rentalItem = rentalItemRepository.findAllByBookId(id);
            rentalItem.forEach(item -> item.setBook(null));
            rentalItemRepository.saveAll(rentalItem);

            bookRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new NewException("Delete book with id: " + id + " fail");
        }
        
    }

    @Override
    public Long getQuantityBookActive() {
        return bookRepository.countByStatus(EntityStatusEnum.Active.toString());
    };

    @Override
    public Long getQuantityBookDelete() {
        return bookRepository.countByStatus(EntityStatusEnum.Delete.toString());
    };

    @Override
    public SearchResponse searchBook(
            String keyword,
            Long categoryId,
            Long authorId,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String bookStatus,
            Pageable pageable
    ) {
        Specification<Book> spec = BookSpecification.filterBooks(
                keyword,
                categoryId,
                authorId,
                language,
                minPrice,
                maxPrice,
                bookStatus
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
