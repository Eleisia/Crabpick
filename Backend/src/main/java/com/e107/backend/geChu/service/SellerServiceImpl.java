package com.e107.backend.geChu.service;

import com.e107.backend.geChu.domain.entity.Discount;
import com.e107.backend.geChu.domain.entity.TopSeller;
import com.e107.backend.geChu.domain.repository.DiscountRepository;
import com.e107.backend.geChu.domain.repository.TopSellerRepository;
import com.e107.backend.geChu.dto.response.DiscountRespDto;
import com.e107.backend.geChu.dto.response.TopSellerRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {
    private final TopSellerRepository topSellerRepository;
    private final DiscountRepository discountRepository;

    @Override
    public List<TopSellerRespDto> findAllTopSeller() {
        List<TopSeller> list = topSellerRepository.findAll();
        return list.stream()
                .map(TopSellerRespDto::of)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<DiscountRespDto> findAllDiscount() {
        List<Discount> list = discountRepository.findAll();
        return list.stream()
                .map(DiscountRespDto::of)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Override
    public void updateTopSeller() {
        String url = "https://store.steampowered.com/api/featuredcategories/?l=koreana";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        ResponseEntity<?> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        org.json.JSONObject jo = new org.json.JSONObject(resultMap.getBody().toString());
        org.json.JSONObject jo2 = (org.json.JSONObject)jo.get("top_sellers");
        org.json.JSONArray jo3 = (org.json.JSONArray)jo2.get("items");
        topSellerRepository.deleteAllInBatch();
        for (Object j:jo3) {
            if(topSellerRepository.existsByGameId(Long.parseLong(((org.json.JSONObject) j).get("id").toString()))){
                continue;
            }
            if(!isGame(Long.parseLong(((org.json.JSONObject) j).get("id").toString()))){
                continue;
            }
            topSellerRepository.save(TopSeller.builder()
                    .gameId(Long.parseLong(((org.json.JSONObject) j).get("id").toString()))
                    .name(((org.json.JSONObject) j).get("name").toString())
                    .discountPercent(Long.parseLong(((org.json.JSONObject) j).get("discount_percent").toString()))
                    .originalPrice(Long.parseLong(((org.json.JSONObject) j).get("original_price").toString()) / 100)
                    .finalPrice(Long.parseLong(((org.json.JSONObject) j).get("final_price").toString()) / 100)
                    .imageLink(((org.json.JSONObject) j).get("large_capsule_image").toString())
                    .build());
        }

    }

    @Override
    public void updateDiscountSeller() {
        String url = "https://store.steampowered.com/api/featuredcategories/?l=koreana";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        ResponseEntity<?> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        org.json.JSONObject jo = new org.json.JSONObject(resultMap.getBody().toString());
        org.json.JSONObject jo2 = (org.json.JSONObject)jo.get("specials");
        org.json.JSONArray jo3 = (org.json.JSONArray)jo2.get("items");
        discountRepository.deleteAllInBatch();
        for (Object j:jo3) {
            if(discountRepository.existsByGameId(Long.parseLong(((org.json.JSONObject) j).get("id").toString()))){
                continue;
            }
            if(!isGame(Long.parseLong(((org.json.JSONObject) j).get("id").toString()))){
                continue;
            }
            discountRepository.save(Discount.builder()
                    .gameId(Long.parseLong(((org.json.JSONObject) j).get("id").toString()))
                    .name(((org.json.JSONObject) j).get("name").toString())
                    .discountPercent(Long.parseLong(((org.json.JSONObject) j).get("discount_percent").toString()))
                    .originalPrice(Long.parseLong(((org.json.JSONObject) j).get("original_price").toString()) / 100)
                    .finalPrice(Long.parseLong(((org.json.JSONObject) j).get("final_price").toString()) / 100)
                    .imageLink(((org.json.JSONObject) j).get("large_capsule_image").toString())
                    .build());
        }

    }

    @Override
    public boolean isGame(Long gameId){
        String url = "https://store.steampowered.com/api/appdetails?appids=" + gameId + "&l=koreana";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        ResponseEntity<?> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        org.json.JSONObject jo = new org.json.JSONObject(resultMap.getBody().toString());
        org.json.JSONObject jo2 = (org.json.JSONObject) jo.get(gameId.toString());
        String jo5 = jo2.get("success").toString();
        if(jo5.equals("false")){
            System.out.println("CHKKK");
            return false;
        }
        org.json.JSONObject jo3 = (org.json.JSONObject) jo2.get("data");
        String jo4 = jo3.get("type").toString();
        if(jo4.equals("game")){
            return true;
        }
        return false;
    }

}
