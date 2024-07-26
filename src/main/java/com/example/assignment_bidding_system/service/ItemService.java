package com.example.assignment_bidding_system.service;

import com.example.assignment_bidding_system.entity.Bid;
import com.example.assignment_bidding_system.entity.Item;
import com.example.assignment_bidding_system.service.serviceImpl.ItemServiceImpl;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);
    List<Bid> getItemBids(Long itemId);
}
