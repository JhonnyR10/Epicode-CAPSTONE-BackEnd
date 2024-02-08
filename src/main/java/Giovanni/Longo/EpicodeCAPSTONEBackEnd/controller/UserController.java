package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.BadRequestException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterResponseDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String orderBy) {
        return userService.getUsers(page, size, orderBy);
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
}
