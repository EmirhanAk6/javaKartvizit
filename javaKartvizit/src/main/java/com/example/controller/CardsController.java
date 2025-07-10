package com.example.controller;

import com.example.dto.cardDto.CardRequest;
import com.example.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/{userId}/cards")
@CrossOrigin(origins = "*")
public class CardsController {
    
    @Autowired
    private CardsService cardsService;
    
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Business Card API is working!");
    }
    
    // Kullanıcının kartvizitlerini getir
    @GetMapping("/my-cards")
    public ResponseEntity<?> getUserCards(
    		@PathVariable Integer userId,
            HttpServletRequest request) {
        try {
            /* Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            } */
            
            Integer tokenUserId = (Integer) request.getAttribute("userId");
            if (!tokenUserId.equals(userId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You can only access your own cards");
                return ResponseEntity.status(403).body(error);
            }
            var cards = cardsService.getUserCards(userId);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // Kartvizit oluştur
    @PostMapping("/create")
    public ResponseEntity<?> createCard(
    		@PathVariable Integer userId,
            @Valid @RequestBody CardRequest request,
            HttpServletRequest httpRequest,
            BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            /* Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            } */
            
       
            Integer tokenUserId = (Integer) httpRequest.getAttribute("userId");
            
            // URL'deki userId ile token'daki userId'nin eşleşmesini kontrol et
            if (!tokenUserId.equals(userId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You can only create cards for yourself");
                return ResponseEntity.status(403).body(error);
            }
            var response = cardsService.createCard(request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // TOKEN YAPISINA FİLTRELEME
    

    
    // BU KALDIRILABİLİR
   /*  @GetMapping("/public")
    public ResponseEntity<?> getPublicCards() {
        try {
            var cards = cardsService.getPublicCards();
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    } */ 
    
    
    // KONTROL EKLEMEK GEREKİR CARDIDININ İÇİNDEKİ USER ID İLE JWT TOKENİN İÇİNDEKİ USER ID KONTROLÜ 
    
    // Kartvizit güncelle
    @PutMapping("/my-cards/{cardId}")
    public ResponseEntity<?> updateCard(
    		@PathVariable Integer userId,
            @PathVariable Integer cardId,
            @Valid @RequestBody CardRequest request,
            HttpServletRequest httpRequest,
            BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            /* Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            } */
            
            Integer tokenUserId = (Integer) httpRequest.getAttribute("userId");
            
            // URL'deki userId ile token'daki userId'nin eşleşmesini kontrol et
            if (!tokenUserId.equals(userId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You can only update your own cards");
                return ResponseEntity.status(403).body(error);
            }
            var response = cardsService.updateCard(cardId, request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // Kartvizit sil
    @DeleteMapping("/my-cards/{cardId}")
    public ResponseEntity<?> deleteCard(
    		@PathVariable Integer userId,
            @PathVariable Integer cardId,
            HttpServletRequest httpRequest) {
        try {
            /* Token'ı kontrol et
            String token = extractToken(authHeader);
            if (!jwtService.validateToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            } */
            
            Integer tokenUserId = (Integer) httpRequest.getAttribute("userId");
            
            // URL'deki userId ile token'daki userId'nin eşleşmesini kontrol et
            if (!tokenUserId.equals(userId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You can only delete your own cards");
                return ResponseEntity.status(403).body(error);
            }
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
    

    
    /* Token'ı header'dan çıkar
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Authorization header is missing or invalid");
    }*/
}
