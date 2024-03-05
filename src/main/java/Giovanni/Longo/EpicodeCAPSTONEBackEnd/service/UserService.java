package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.BadRequestException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.MatchAlreadyExistsException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinaryUploader;

    private PasswordEncoder bcrypt = new BCryptPasswordEncoder(11);

    public Page<User> getUsers(int page, int size, String orderBy) {
        if (size >= 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userRepository.findAll(pageable);
    }

    public User save(UserRegisterDTO body) {
        userRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
        });
        User nuovoUtente = new User();
        nuovoUtente.setNome(body.nome());
        nuovoUtente.setCognome(body.cognome());
        nuovoUtente.setEmail(body.email());
        nuovoUtente.setUsername(body.username());
        nuovoUtente.setPassword(bcrypt.encode(body.password()));
        nuovoUtente.setAvatar("https://ui-avatars.com/api/?name=" + nuovoUtente.getNome() + "+" + nuovoUtente.getCognome());
        if (body.role() != null) {
            nuovoUtente.setRole(body.role());
        }
        return userRepository.save(nuovoUtente);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(long id) {
        User found = this.findById(id);
        userRepository.delete(found);
    }

    @Transactional
    public User findbyIdAndUpdate(long id, User body) {
        User found = this.findById(id);
        found.setCognome(body.getCognome());
        found.setNome(body.getNome());
        found.setEmail(body.getEmail());
        found.setUsername(body.getUsername());
        return userRepository.save(found);

    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

    public User uploadAvatar(long id, MultipartFile file) throws IOException {
        User found = this.findById(id);
        String avatarURL = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(avatarURL);
        return userRepository.save(found);
    }

    public void deleteStatisticaGioco(Long userId, Long statisticaId) {
        User userFound = this.findById(userId);

        if (userFound != null) {
            userFound.removeStatisticaGioco(statisticaId);
            userRepository.save(userFound);
        } else {
            System.out.println("Utente non trovato con ID: " + userId);
        }
    }

    public User addMatch(Long userId, Long matchId) {
        User user = findById(userId);
        User match = findById(matchId);

        // Verifica se l'amico è già presente nella lista
        if (!user.getMatches().contains(match)) {
            user.addMatch(match);
            return userRepository.save(user);
        } else {
            // L'amico è già presente, lanci l'eccezione personalizzata
            throw new MatchAlreadyExistsException("Match with ID " + matchId + " already exists in the user's matches.");
        }
    }

    public List<User> getAllMatches(Long userId) {
        User user = findById(userId);
        return new ArrayList<>(user.getMatches());
    }

    public User removeMatch(Long userId, Long matchId) {
        User user = findById(userId);
        User match = findById(matchId);

        user.removeMatch(match);

        return userRepository.save(user);
    }

    @Transactional
    public void findNewsByIdAndDelete(long userId, long newsId) {
        User user = findById(userId);
        if (user != null) {
            user.removeNews(newsId);
            userRepository.save(user);
        } else {
            System.out.println("Utente non trovato con ID: " + userId);
        }
    }
}
