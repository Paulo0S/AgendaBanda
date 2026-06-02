package br.com.fatec.agendabanda.controller;

import br.com.fatec.agendabanda.dto.AuthDtos.LoginRequest;
import br.com.fatec.agendabanda.dto.AuthDtos.LoginResponse;
import br.com.fatec.agendabanda.security.TokenService;
import br.com.fatec.agendabanda.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        tokenService.removerToken(tokenService.extrairToken(request));
        return Map.of("mensagem", "Logout realizado com sucesso.");
    }
}
