package com.e107.backend.global;

import com.e107.backend.geChu.service.NewsService;
import com.e107.backend.geChu.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;

@Component // component 스캔을 가능하게 한다.
@RequiredArgsConstructor
public class Scheduler {

    private final NewsService newsService;
    private final SellerService sellerService;
    @Scheduled(fixedRate = 60000 * 5) // 메소드 호출이 종료되는 시간에서 5분 이후 재 호출
    public void updateNews() throws IOException {
        System.out.println("fixedRate");
        System.out.println("news update job start : " + LocalTime.now());
        newsService.updateNews();
        System.out.println("news update job end : " + LocalTime.now());
    }

    @Scheduled(fixedRate = 60000 * 5) // 메소드 호출이 종료되는 시간에서 5분 이후 재 호출
    public void updateTopSeller() throws IOException {
        System.out.println("topSeller update job start : " + LocalTime.now());
        sellerService.updateTopSeller();
        System.out.println("topSeller update job end : " + LocalTime.now());
    }

    @Scheduled(fixedRate = 60000 * 5) // 메소드 호출이 종료되는 시간에서 5분 이후 재 호출
    public void updateDiscount() throws IOException {
        System.out.println("topSeller update job start : " + LocalTime.now());
        sellerService.updateDiscountSeller();
        System.out.println("topSeller update job end : " + LocalTime.now());
    }
}
