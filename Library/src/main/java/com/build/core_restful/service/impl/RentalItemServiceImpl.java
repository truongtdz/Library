package com.build.core_restful.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.RentalItem;
import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.User;
import com.build.core_restful.domain.response.BookLateResponse;
import com.build.core_restful.repository.RentalItemRepository;
import com.build.core_restful.service.RentalItemService;
import com.build.core_restful.system.EmailUtil;
import com.build.core_restful.util.enums.ItemStatusEnum;

@Service
public class RentalItemServiceImpl implements RentalItemService{
    private final RentalItemRepository rentalItemRepository;
    private final EmailUtil emailUtil;

    public RentalItemServiceImpl (
        RentalItemRepository rentalItemRepository,
        EmailUtil emailUtil
    ){
        this.rentalItemRepository = rentalItemRepository;
        this.emailUtil = emailUtil;
    };

    @Override
    public void sendEmailLateBook() {
        Instant now = Instant.now();
        Instant threeDaysLater = now.plus(3, ChronoUnit.DAYS);

        List<RentalItem> rentalItems = rentalItemRepository.findItemsDueWithinThreeDaysByStatus(
            now, threeDaysLater, ItemStatusEnum.Rented.toString());

        Map<Long, List<RentalItem>> itemsByUser = rentalItems.stream()
            .collect(Collectors.groupingBy(item -> item.getRentalOrder().getUser().getId()));

        itemsByUser.forEach((userId, userItems) -> {
            RentalOrder rentalOrder = userItems.get(0).getRentalOrder();
            User user = rentalOrder.getUser();
            
            List<BookLateResponse> bookLateResponses = userItems.stream()
                .map(item -> BookLateResponse.builder()
                    .email(user.getEmail())
                    .bookName(item.getBookName())
                    .imageUrl(item.getImageUrl())
                    .rentalOrderId(item.getRentalOrder().getId().toString())
                    .rentalDate(item.getRentalDate())
                    .rentedDate(item.getRentedDate())
                    .build())
                .collect(Collectors.toList());

            emailUtil.sendEmailLateBook(
                user.getEmail(), 
                user.getFullName(), 
                bookLateResponses
            );
        });
    }
    
}
