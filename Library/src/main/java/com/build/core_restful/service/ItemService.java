package com.build.core_restful.service;

import com.build.core_restful.domain.request.ItemRequest;
import com.build.core_restful.domain.response.ItemResponse;
import com.build.core_restful.domain.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    boolean existItemById(Long id);
    PageResponse<Object> getAllItems(Pageable pageable);
    ItemResponse getItemById(Long id);
    ItemResponse createItem(ItemRequest item);
    ItemResponse updateItem(ItemRequest item);
    void deleteItem(Long id);
}
