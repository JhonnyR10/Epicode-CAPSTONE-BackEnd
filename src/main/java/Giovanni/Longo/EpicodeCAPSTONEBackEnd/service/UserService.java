package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.BadRequestException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
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
}
