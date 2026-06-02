package br.com.fatec.agendabanda.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private final Map<String, SessaoUsuario> sessoes = new ConcurrentHashMap<>();

    public String criarToken(SessaoUsuario usuario) {
        String token = UUID.randomUUID().toString();
        sessoes.put(token, usuario);
        return token;
    }

    public Optional<SessaoUsuario> buscarSessao(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessoes.get(token));
    }

    public void removerToken(String token) {
        sessoes.remove(token);
    }

    public String extrairToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return request.getHeader("X-Auth-Token");
    }

    public SessaoUsuario sessaoObrigatoria(HttpServletRequest request) {
        String token = extrairToken(request);
        return buscarSessao(token).orElseThrow(() -> new IllegalArgumentException("Sessão inválida ou expirada."));
    }
}
