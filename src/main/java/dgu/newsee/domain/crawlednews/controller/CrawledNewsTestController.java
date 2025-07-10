package dgu.newsee.domain.crawlednews.controller;

import dgu.newsee.domain.crawlednews.service.CrawledNewsScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/crawled-news")
@RequiredArgsConstructor
public class CrawledNewsTestController {

    private final CrawledNewsScheduler scheduler;

    @PostMapping("/run")
    public String runSchedulerManually() {
        scheduler.crawlNewsPeriodically();
        return "크롤링 스케줄러 수동 실행 완료";
    }
}
