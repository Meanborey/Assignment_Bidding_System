package com.example.assignment_bidding_system.controller;


import com.example.assignment_bidding_system.entity.Bid;
import com.example.assignment_bidding_system.entity.Item;
import com.example.assignment_bidding_system.service.BidService;
import com.example.assignment_bidding_system.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private BidService bidService;

    @PostMapping
    public Item postItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @GetMapping("/{itemId}/bids")
    public List<Bid> getBidHistory(@PathVariable Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        return (List<Bid>) bidService.getHighestBid(item);
    }
}
