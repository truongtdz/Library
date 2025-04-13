package com.build.core_restful.service.impl;

import com.build.core_restful.domain.RentalItem;
import com.build.core_restful.domain.request.ItemRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.ItemResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.ItemRepository;
import com.build.core_restful.service.ItemService;
import com.build.core_restful.util.mapper.ItemMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public boolean existItemById(Long id) {
        return itemRepository.existsById(id);
    }

    @Override
    public PageResponse<Object> getAllItems(Pageable pageable) {
        Page<ItemResponse> page = itemRepository.findAll(pageable).map(itemMapper::toItemResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(page.getContent())
                .build();
    }

    @Override
    public ItemResponse getItemById(Long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toItemResponse)
                .orElse(null);
    }

    @Override
    public ItemResponse createItem(ItemRequest itemRequest) {
        RentalItem item = itemMapper.toItem(itemRequest);
        return itemMapper.toItemResponse(itemRepository.save(item));
    }

    @Override
    public ItemResponse updateItem(ItemRequest itemRequest) {
        RentalItem item = itemRepository.findById(itemRequest.getId()).orElse(null);
        if (item != null) {
            itemMapper.updateItem(item, itemRequest);
            return itemMapper.toItemResponse(itemRepository.save(item));
        }
        return null;
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
