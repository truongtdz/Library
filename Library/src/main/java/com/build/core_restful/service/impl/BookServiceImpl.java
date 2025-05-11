package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Image;
import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.CategoryRepository;
import com.build.core_restful.repository.AuthorsRepository;
import com.build.core_restful.repository.ImageRepository;
import com.build.core_restful.service.BookService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BookMapper;
import com.build.core_restful.util.upload.CloudinaryUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorsRepository authorsRepository;
    private final ImageRepository imageRepository;
    private final BookMapper bookMapper;
    private final CloudinaryUpload cloudinaryUpload;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, AuthorsRepository authorsRepository, ImageRepository imageRepository, BookMapper bookMapper, CloudinaryUpload cloudinaryUpload) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.authorsRepository = authorsRepository;
        this.imageRepository = imageRepository;
        this.bookMapper = bookMapper;
        this.cloudinaryUpload = cloudinaryUpload;
    }

    @Override
    public PageResponse<Object> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);

        List<BookResponse> bookResponses = bookPage.getContent().stream().map(book -> {
            BookResponse response = bookMapper.toBookResponse(book);

            List<BookResponse.ImageResponse> imageList = book.getImages().stream()
                    .map(image -> BookResponse.ImageResponse.builder()
                            .isCover(image.isCover())
                            .url(image.getUrl())
                            .build())
                    .collect(Collectors.toList());

            response.setImageList(imageList);
            return response;
        }).collect(Collectors.toList());

        return PageResponse.builder()
                .page(bookPage.getNumber())
                .size(bookPage.getSize())
                .content(bookResponses)
                .build();
    }


    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NewException("Book with id: " + id + " not found"));

        BookResponse bookResponse = bookMapper.toBookResponse(book);

        bookResponse.setImageList(
                book.getImages().stream()
                        .map(image -> BookResponse.ImageResponse.builder()
                                .isCover(image.isCover())
                                .url(image.getUrl())
                                .build())
                        .collect(Collectors.toList())
        );

        return bookResponse;
    }


    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        Book book = bookMapper.toBook(bookRequest);

        if (!categoryRepository.existsById(bookRequest.getCategoryId())) {
            throw new NewException("Category id " + bookRequest.getCategoryId() + " not found");
        }

        if (!authorsRepository.existsById(bookRequest.getAuthorsId())) {
            throw new NewException("Author id " + bookRequest.getAuthorsId() + " not found");
        }

        book.setCategory(categoryRepository.findById(bookRequest.getCategoryId()).get());
        book.setAuthors(authorsRepository.findById(bookRequest.getAuthorsId()).get());

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NewException("Book with id: " + id + " not found"));

        bookMapper.updateBook(book, bookRequest);

        if (categoryRepository.existsById(bookRequest.getCategoryId())) {
            book.setCategory(categoryRepository.findById(bookRequest.getCategoryId()).get());
        }

        if (authorsRepository.existsById(bookRequest.getAuthorsId())) {
            book.setAuthors(authorsRepository.findById(bookRequest.getAuthorsId()).get());
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
    public boolean uploadImages(Long id, List<MultipartFile> images) {
        if (!bookRepository.existsById(id)) {
            throw new NewException("Book with id: " + id + " not found");
        }

        Book currentBook = bookRepository.findById(id).get();

        for (MultipartFile item : images){
            try{
                String url = cloudinaryUpload.uploadFile(item);
                Image image = Image.builder()
                        .url(url)
                        .book(currentBook)
                        .build();
                imageRepository.save(image);
            } catch (IOException e) {
                throw new NewException("Upload file fail");
            }
        }
        return true;
    }

    @Override
    public void setImageCover(Long imageId, Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NewException("Book with id: " + bookId + " not found");
        }

        Image image = imageRepository.findByIdAndBookId(imageId, bookId);

        image.setCover(true);

        imageRepository.save(image);
    }

    @Override
    public SearchResponse searchBook(String keyword, Pageable pageable) {
        Page<Book> page;
        if (keyword != null && !keyword.isEmpty()) {
            page = bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else {
            page = bookRepository.findAll(pageable);
        }
        Page<BookResponse> pageResponse = page.map(bookMapper::toBookResponse);
        return SearchResponse.builder()
                .keyword(keyword)
                .result(PageResponse.builder()
                                .page(page.getNumber())
                                .size(page.getSize())
                                .content(pageResponse.getContent())
                                .build())
                .build();
    }
}
