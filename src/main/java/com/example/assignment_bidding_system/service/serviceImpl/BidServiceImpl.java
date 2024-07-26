package com.example.assignment_bidding_system.service.serviceImpl;


import com.example.assignment_bidding_system.entity.Bid;
import com.example.assignment_bidding_system.entity.Item;
import com.example.assignment_bidding_system.entity.User;
import com.example.assignment_bidding_system.repository.BidRepository;
import com.example.assignment_bidding_system.repository.ItemRepository;
import com.example.assignment_bidding_system.repository.UserRepository;
import com.example.assignment_bidding_system.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Map<Long, LocalDateTime> activeSessions = new ConcurrentHashMap<>();

    @Override
    public Bid placeBid(Bid bid) {
        bid.setTimestamp(LocalDateTime.now());
        return bidRepository.save(bid);
    }

    @Override
    public Bid getHighestBid(Item item) {
        return bidRepository.findTopByItemOrderByAmountDesc(item);
    }

    @Scheduled(fixedRate = 120000) // Run every 2 minutes
    public void startBidSession() {
        List<Item> items = getAllItems(); // Implement getAllItems to fetch all items

        for (Item item : items) {
            if (activeSessions.containsKey(item.getId())) {
                // End the session and announce the winner
                endBidSession(item);
            }
            // Start a new session
            activeSessions.put(item.getId(), LocalDateTime.now());
        }
    }

    @Override
    public List<Bid> getBidsByUser(User user) {
        return bidRepository.findByBidder(user);
    }

    @Override
    public List<Bid> getWinningBidsByUser(User user) {
        // Retrieve all bids by the user
        List<Bid> userBids = bidRepository.findByBidder(user);

        // Collect highest bid amounts per item
        Map<Item, Bid> highestBids = userBids.stream()
                .collect(Collectors.toMap(
                        Bid::getItem,
                        bid -> bidRepository.findTopByItemOrderByAmountDesc(bid.getItem()),
                        (existing, replacement) -> existing // Choose the existing bid in case of duplicates
                ));

        // Filter bids where the bid amount matches the highest bid for the item
        return userBids.stream()
                .filter(bid -> {
                    Bid highestBid = highestBids.get(bid.getItem());
                    return highestBid != null && bid.getAmount().equals(highestBid.getAmount());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Bid> getLosingBidsByUser(User user) {
        // Retrieve all bids by the user
        List<Bid> userBids = bidRepository.findByBidder(user);

        // Collect highest bid amounts per item
        Map<Item, Bid> highestBids = userBids.stream()
                .collect(Collectors.toMap(
                        Bid::getItem,
                        bid -> bidRepository.findTopByItemOrderByAmountDesc(bid.getItem()),
                        (existing, replacement) -> existing // Choose the existing bid in case of duplicates
                ));

        // Filter bids where the bid amount does not match the highest bid for the item
        return userBids.stream()
                .filter(bid -> {
                    Bid highestBid = highestBids.get(bid.getItem());
                    return highestBid != null && !bid.getAmount().equals(highestBid.getAmount());
                })
                .collect(Collectors.toList());
    }


    private void endBidSession(Item item) {
        Bid highestBid = getHighestBid(item);
        if (highestBid != null) {
            announceWinner(highestBid.getBidder(), highestBid.getAmount());
        }
        activeSessions.remove(item.getId());
    }

    private void announceWinner(User bidder, BigDecimal amount) {
        System.out.println("Winner: " + bidder.getUsername() + " with bid amount: " + amount);
    }

    private List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}