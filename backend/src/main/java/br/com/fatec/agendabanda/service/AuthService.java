package br.com.fatec.agendabanda.service;

import br.com.fatec.agendabanda.dto.AuthDtos.LoginRequest;
import br.com.fatec.agendabanda.dto.AuthDtos.LoginResponse;
import br.com.fatec.agendabanda.repository.MembroRepository;
import br.com.fatec.agendabanda.security.SessaoUsuario;
import br.com.fatec.agendabanda.security.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MembroRepository membroRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MapperService mapperService;

    public AuthService(MembroRepository membroRepository, BCryptPasswordEncoder passwordEncoder, TokenService tokenService, MapperService mapperService) {
        this.membroRepository = membroRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.mapperService = mapperService;
    }

    public LoginResponse login(LoginRequest request) {
        var membro = membroRepository.findByLogin(request.login())
                .orElseThrow(() -> new IllegalArgumentException("Login ou senha inválidos."));

        if (!Boolean.TRUE.equals(membro.getAtivo())) {
            throw new IllegalArgumentException("Usuário desativado.");
        }
        if (!passwordEncoder.matches(request.senha(), membro.getSenha())) {
            throw new IllegalArgumentException("Login ou senha inválidos.");
        }

        var sessao = new SessaoUsuario(membro.getCodigo(), membro.getNome(), membro.getLogin(), Boolean.TRUE.equals(membro.getAdministrador()));
        String token = tokenService.criarToken(sessao);
        return new LoginResponse(token, mapperService.membroDto(membro));
    }
}
