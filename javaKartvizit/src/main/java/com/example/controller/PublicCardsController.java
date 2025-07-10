package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.CardsService;


public class PublicCardsController {
	
    @Autowired
    private CardsService cardsService;
    


    // Belirli bir kartviziti getir
    @GetMapping("/{cardId}")
    public ResponseEntity<?> getCard(@PathVariable Integer cardId) {
        try {
            var card = cardsService.getCardById(cardId);
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    // Kartvizit arama
    @GetMapping("/search")
    public ResponseEntity<?> searchCards(@RequestParam String query) {
        try {
            var cards = cardsService.searchCards(query);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
