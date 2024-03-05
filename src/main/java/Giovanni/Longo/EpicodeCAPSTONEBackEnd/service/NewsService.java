package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.News;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.NewsDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository.NewsRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private Cloudinary cloudinaryUploader;

    public News save(NewsDTO body, Long userId) {
        User found = userService.findById(userId);
        News nuovaNews = new News();
        nuovaNews.setText(body.text());
        nuovaNews.setTitle(body.title());
        nuovaNews.setReadingTimeMinutes(body.readingTimeMinutes());
        nuovaNews.setCreationDate(LocalDate.now());
        nuovaNews.setCoverImageLink("https://ui-avatars.com/api/?name=" + nuovaNews.getTitle() + "+" + nuovaNews.getAuthor());
        nuovaNews.setAuthor(found);
        newsRepository.save(nuovaNews);
        found.getNews().add(nuovaNews);

        return nuovaNews;
    }

    public News findById(long id) {
        return newsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public void findByIdAndDelete(long id) {
        News found = this.findById(id);
        newsRepository.delete(found);
    }

    public News uploadAvatar(long id, MultipartFile file) throws IOException {
        News found = this.findById(id);
        String avatarURL = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setCoverImageLink(avatarURL);
        return newsRepository.save(found);
    }

    @Transactional
    public News findbyIdAndUpdate(News body, Long newsId) {
        News found = this.findById(newsId);

        found.setText(body.getText());
        found.setTitle(body.getTitle());
        found.setReadingTimeMinutes(body.getReadingTimeMinutes());
        found.setCreationDate(LocalDate.now());
        return newsRepository.save(found);
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> findNewsByTitle(String keyword) {
        return newsRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
