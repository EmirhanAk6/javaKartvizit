package com.example.service;

import com.example.dto.cardDto.CardRequest;
import com.example.dto.cardDto.CardResponse;
import com.example.model.CardsModel;
import com.example.model.UsersModel;
import com.example.repository.CardsRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardsService {
    
    @Autowired
    private CardsRepository cardsRepository;
    
    @Autowired
    private UsersRepository usersRepository;
    
    // Kartvizit oluştur
    public CardResponse createCard(CardRequest request, Integer userId) {
        UsersModel user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CardsModel card = new CardsModel();
        mapRequestToCard(request, card);
        card.setUser(user);
        
        CardsModel savedCard = cardsRepository.save(card);
        return mapCardToResponse(savedCard);
    }
    
    // Kullanıcının kartvizitlerini getir
    public List<CardResponse> getUserCards(Integer userId) {
        UsersModel user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<CardsModel> cards = cardsRepository.findByUserOrderByIdDesc(user);
        return cards.stream().map(this::mapCardToResponse).collect(Collectors.toList());
    }
    
    // Tüm kartvizitleri getir
    public List<CardResponse> getPublicCards() {
        List<CardsModel> cards = cardsRepository.findAll();
        return cards.stream().map(this::mapCardToResponse).collect(Collectors.toList());
    }
    
    // Kartvizit ID ile getir
    public CardResponse getCardById(Integer cardId) {
        CardsModel card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        return mapCardToResponse(card);
    }
    
    // Kartvizit güncelle
    public CardResponse updateCard(Integer cardId, CardRequest request, Integer userId) {
        CardsModel card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        // Yalnızca sahip güncelleyebilir
        if (!card.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own cards");
        }
        
        mapRequestToCard(request, card);
        CardsModel updatedCard = cardsRepository.save(card);
        return mapCardToResponse(updatedCard);
    }
    
    // Kartvizit sil
    public void deleteCard(Integer cardId, Integer userId) {
        CardsModel card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        // Yalnızca sahip silebilir
        if (!card.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own cards");
        }
        
        cardsRepository.delete(card);
    }
    
    // Kartvizit arama
    public List<CardResponse> searchCards(String query) {
        List<CardsModel> cards = cardsRepository.searchPublicCards(query);
        return cards.stream().map(this::mapCardToResponse).collect(Collectors.toList());
    }
    
    // Request'i Card'a map etme
    private void mapRequestToCard(CardRequest request, CardsModel card) {
        card.setFullName(request.getFullName());
        card.setJobTitle(request.getJobTitle());
        card.setPhoneNumber(request.getPhone());
        card.setEmail(request.getEmail());
        card.setAddress(request.getAddress());
    }
    
    // Card'ı Response'a map etme
    private CardResponse mapCardToResponse(CardsModel card) {
        CardResponse response = new CardResponse();
        response.setId(card.getId());
        response.setFullName(card.getFullName());
        response.setJobTitle(card.getJobTitle());
        response.setPhone(card.getPhoneNumber());
        response.setEmail(card.getEmail());
        response.setAddress(card.getAddress());
        response.setOwnerUsername(card.getUser().getUsername());
        return response;
    }
}