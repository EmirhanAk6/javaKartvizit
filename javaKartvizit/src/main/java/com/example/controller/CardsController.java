package com.example.controller;

import com.example.dto.cardDto.CardRequest;
import com.example.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.example.service.JwtService;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardsController {
    
    @Autowired
    private CardsService cardsService;
    
    @Autowired
    private JwtService jwtService;
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Business Card API is working!");
    }
    
    // Kartvizit oluştur
    @PostMapping("/create")
    public ResponseEntity<?> createCard(
            @Valid @RequestBody CardRequest request,
            @RequestHeader("Authorization") String authHeader,
            BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            // Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            }
            
            Integer userId = jwtService.getUserIdFromToken(token);
            var response = cardsService.createCard(request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // TOKEN YAPISINA FİLTRELEME
    
    // Kullanıcının kartvizitlerini getir
    @GetMapping("/my-cards")
    public ResponseEntity<?> getMyCards(@RequestHeader("Authorization") String authHeader) {
        try {
            // Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            }
            
            Integer userId = jwtService.getUserIdFromToken(token);
            var cards = cardsService.getUserCards(userId);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // BU KALDIRILABİLİR
    @GetMapping("/public")
    public ResponseEntity<?> getPublicCards() {
        try {
            var cards = cardsService.getPublicCards();
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    
    // KONTROL EKLEMEK GEREKİR CARDIDININ İÇİNDEKİ USER ID İLE JWT TOKENİN İÇİNDEKİ USER ID KONTROLÜ 
    
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
    
    // Kartvizit güncelle
    @PutMapping("/{cardId}")
    public ResponseEntity<?> updateCard(
            @PathVariable Integer cardId,
            @Valid @RequestBody CardRequest request,
            @RequestHeader("Authorization") String authHeader,
            BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            // Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            }
            
            Integer userId = jwtService.getUserIdFromToken(token);
            var response = cardsService.updateCard(cardId, request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // Kartvizit sil
    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(
            @PathVariable Integer cardId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            }
            
            Integer userId = jwtService.getUserIdFromToken(token);
            cardsService.deleteCard(cardId, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Card deleted successfully");
            return ResponseEntity.ok(response);
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
    
    // Token'ı header'dan çıkar
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Authorization header is missing or invalid");
    }
}
