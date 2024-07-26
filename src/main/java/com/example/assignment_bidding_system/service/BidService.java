package com.example.assignment_bidding_system.service;

import com.example.assignment_bidding_system.entity.Bid;
import com.example.assignment_bidding_system.entity.Item;
import com.example.assignment_bidding_system.entity.User;

import java.util.List;


    public interface BidService {
        Bid placeBid(Bid bid);
        Bid getHighestBid(Item item);
        void startBidSession();

        List<Bid> getBidsByUser(User user);

        List<Bid> getWinningBidsByUser(User user);

        List<Bid> getLosingBidsByUser(User user);
    }

