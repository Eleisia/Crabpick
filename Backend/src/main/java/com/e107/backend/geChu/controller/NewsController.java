package com.e107.backend.geChu.controller;

import com.e107.backend.geChu.dto.response.NewsRespDto;
import com.e107.backend.geChu.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<NewsRespDto>> getAllNews() {
        List<NewsRespDto> list = newsService.getNews();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
