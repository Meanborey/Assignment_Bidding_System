package com.example.assignment_bidding_system.repository;

import com.example.assignment_bidding_system.entity.Bid;
import com.example.assignment_bidding_system.entity.Item;
import com.example.assignment_bidding_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByItem(Item item);
    List<Bid> findByBidder(User bidder);
    Bid findTopByItemOrderByAmountDesc(Item item);
}
