package br.com.fatec.agendabanda.controller;

import br.com.fatec.agendabanda.dto.CidadeDto;
import br.com.fatec.agendabanda.dto.LocalDto;
import br.com.fatec.agendabanda.dto.LocalRequest;
import br.com.fatec.agendabanda.security.TokenService;
import br.com.fatec.agendabanda.service.LocalService;
import br.com.fatec.agendabanda.service.PermissaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LocalController {
    private final LocalService service;
    private final TokenService tokenService;
    private final PermissaoService permissaoService;

    public LocalController(LocalService service, TokenService tokenService, PermissaoService permissaoService) {
        this.service = service;
        this.tokenService = tokenService;
        this.permissaoService = permissaoService;
    }

    @GetMapping("/cidades")
    public List<CidadeDto> cidades() {
        return service.listarCidades();
    }

    @GetMapping("/locais")
    public List<LocalDto> locais() {
        return service.listarLocais();
    }

    @PostMapping("/locais")
    public LocalDto criar(@Valid @RequestBody LocalRequest request, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        return service.criar(request);
    }

    @PutMapping("/locais/{id}")
    public LocalDto atualizar(@PathVariable Integer id, @Valid @RequestBody LocalRequest request, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        return service.atualizar(id, request);
    }

    @DeleteMapping("/locais/{id}")
    public Map<String, String> excluir(@PathVariable Integer id, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        service.excluir(id);
        return Map.of("mensagem", "Local excluído com sucesso.");
    }
}
