package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.News;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.NewsDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or (#id == principal.userId)")
    public ResponseEntity<?> saveNews(
            @RequestBody NewsDTO newsDTO,
            @PathVariable Long userId) {
        try {
            News savedNews = newsService.save(newsDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNews);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getbyid/{newsId}")
    public ResponseEntity<?> getNewsById(@PathVariable Long newsId) {
        try {
            News news = newsService.findById(newsId);
            return ResponseEntity.ok(news);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{newsId}")
    @PreAuthorize("hasAuthority('ADMIN') or (isAuthenticated())")
    public ResponseEntity<?> updateNews(
            @RequestBody News newsBody,
            @PathVariable Long newsId) {
        try {
            News updatedNews = newsService.findbyIdAndUpdate(newsBody, newsId);
            return ResponseEntity.ok(updatedNews);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable Long newsId) {
        try {
            newsService.findByIdAndDelete(newsId);
            return ResponseEntity.ok("Statistica del gioco eliminata con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{newsId}/upload-cover")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> uploadAvatar(
            @PathVariable Long newsId,
            @RequestParam("file") MultipartFile file) {
        try {
            News updatedNews = newsService.uploadAvatar(newsId, file);
            return ResponseEntity.ok(updatedNews);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<News>> getAllNews() {
        List<News> allNews = newsService.getAllNews();
        return ResponseEntity.ok(allNews);
    }

    @GetMapping("/search")
    public ResponseEntity<List<News>> searchNewsByTitle(@RequestParam String keyword) {
        List<News> newsList = newsService.findNewsByTitle(keyword);
        return ResponseEntity.ok(newsList);
    }
}