package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RentalItem;
import com.build.core_restful.domain.request.ItemRequest;
import com.build.core_restful.domain.response.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    RentalItem toItem(ItemRequest itemRequest);
    ItemResponse toItemResponse(RentalItem item);
    void updateItem(@MappingTarget RentalItem item, ItemRequest itemRequest);
}
