package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.BadRequestException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserLoginDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserLoginResponseDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterResponseDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.AuthService;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body) {
        String accessToken = authService.authenticateUser(body);
        return new UserLoginResponseDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegisterResponseDTO createUser(@RequestBody @Validated UserRegisterDTO newUserPayload, BindingResult validation) {

        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors().stream().toList().toString());
        } else {
            System.out.println(newUserPayload);
            User newUser = userService.save(newUserPayload);

            return new UserRegisterResponseDTO(newUser.getId());
        }
    }
}
