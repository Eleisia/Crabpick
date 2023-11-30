package com.e107.backend.geChu.service;

import com.e107.backend.geChu.domain.entity.News;
import com.e107.backend.geChu.domain.repository.NewsRepository;
import com.e107.backend.geChu.dto.response.NewsRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService{
    private final NewsRepository newsRepository;
    @Override
    public List<NewsRespDto> getNews() {
        List<News> list = newsRepository.findAll();
        ArrayList<NewsRespDto> resp = new ArrayList<>();
        for (News n : list){
            resp.add(NewsRespDto.of(n));
        }
        return resp;
    }

    // 뉴스업데이트 기능 추가 완료
    @Override
    public void updateNews() throws IOException {
        String url;
        int j = 0;
        for (int i = 1; i <= 2; i++) {
            url = "https://www.gamemeca.com/news.php?p=" + i;
            Document doc = Jsoup.connect(url).get();
            Elements elem = doc.select("ul[class=\"list_news\"] li");
            for (Element e : elem) {
                Elements s = e.select("strong[class=\"tit_thumb_h\"]");
                Elements s1 = e.select("strong[class=\"tit_thumb\"]");
                News news = newsRepository.findById(j + 1L).orElseThrow();
                String imageLink = e.select("a img").attr("src");
                String date = e.select("div[class=\"day_news\"]").text();
                if (news.getSubject().equals(s.text()) || news.getSubject().equals(s1.text())) {
                    log.info("NOT CHANGED");
                    return;
                }
                if (s.hasText()) {
                    String subject = s.text();
                    String link = "https://www.gamemeca.com" + s.select("a").attr("href");
                    news.setAttribute(subject, link, imageLink, date);
                }
                if (s1.hasText()) {
                    String subject = s1.text();
                    String link = "https://www.gamemeca.com" + s1.select("a").attr("href");
                    news.setAttribute(subject, link, imageLink, date);
                }
                j++;
            }
        }
    }
}
