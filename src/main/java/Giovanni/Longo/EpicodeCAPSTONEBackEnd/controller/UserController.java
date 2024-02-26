package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.BadRequestException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterResponseDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String orderBy) {
        return userService.getUsers(page, size, orderBy);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        // user contiene le informazioni dell'utente come nome, cognome, ruolo, ecc.
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRegisterResponseDTO createUser(@RequestBody @Validated UserRegisterDTO newUserPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().map(err -> err.getDefaultMessage()).toList().toString());
        }
        User nuovoUtente = userService.save(newUserPayload);
        return new UserRegisterResponseDTO(nuovoUtente.getId());

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (#id == principal.id)")
    public User findByIdAndUpdate(@PathVariable long id, @RequestBody User updateUserPayload) {
        return userService.findbyIdAndUpdate(id, updateUserPayload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN') or (#id == principal.id)")
    public void findByIdAndDelete(@PathVariable long id) {
        userService.findByIdAndDelete(id);
    }

    @PatchMapping("/{userId}/avatar")
    public User uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable long userId) {
        try {
            return userService.uploadAvatar(userId, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{userId}/{statisticaId}")
    public ResponseEntity<String> deleteStatisticaGioco(
            @PathVariable Long userId,
            @PathVariable Long statisticaId) {
        try {
            userService.deleteStatisticaGioco(userId, statisticaId);
            return ResponseEntity.ok("Statistica del gioco eliminata con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Statistica del gioco non trovata.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione della statistica del gioco.");
        }
    }
}
