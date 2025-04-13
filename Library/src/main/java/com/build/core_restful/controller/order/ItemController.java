package com.build.core_restful.controller.order;

import com.build.core_restful.domain.request.ItemRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.ItemService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.exception.NewException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @AddMessage("Get all items")
    public ResponseEntity<PageResponse<Object>> getAllItems(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<Object> items = itemService.getAllItems(pageable);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @AddMessage("Get item by id")
    public ResponseEntity<Object> getItemById(@PathVariable Long id) {
        if (!itemService.existItemById(id)) {
            throw new NewException("Item id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    @AddMessage("Create new item")
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemRequest newItem) {
        return ResponseEntity.ok(itemService.createItem(newItem));
    }

    @PutMapping("/{id}")
    @AddMessage("Update item")
    public ResponseEntity<Object> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest updateItem) {
        if (!itemService.existItemById(id)) {
            throw new NewException("Item id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(itemService.updateItem(updateItem));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete item")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (!itemService.existItemById(id)) {
            throw new NewException("Item id = " + id + " không tồn tại");
        }
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}
