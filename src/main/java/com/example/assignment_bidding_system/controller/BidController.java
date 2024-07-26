package com.example.assignment_bidding_system.controller;

import com.example.assignment_bidding_system.entity.Bid;
import com.example.assignment_bidding_system.entity.Item;
import com.example.assignment_bidding_system.entity.User;
import com.example.assignment_bidding_system.service.BidService;
import com.example.assignment_bidding_system.service.ItemService;
import com.example.assignment_bidding_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Bid placeBid(@RequestBody Bid bid, @AuthenticationPrincipal OAuth2User oauth2User) {
        Optional<User> user = userService.findByUsername(oauth2User.getName());
        bid.setBidder(user.orElseThrow(() -> new RuntimeException("User not found")));
        return bidService.placeBid(bid);
    }

    @GetMapping("/highest/{itemId}")
    public Bid getHighestBid(@PathVariable Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        return bidService.getHighestBid(item);
    }

    @GetMapping("/user/{userId}/bids")
    public List<Bid> getUserBidHistory(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return bidService.getBidsByUser(user);
    }

    @GetMapping("/user/{userId}/wins")
    public List<Bid> getUserWins(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return bidService.getWinningBidsByUser(user);
    }

    @GetMapping("/user/{userId}/losses")
    public List<Bid> getUserLosses(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return bidService.getLosingBidsByUser(user);
    }
}
