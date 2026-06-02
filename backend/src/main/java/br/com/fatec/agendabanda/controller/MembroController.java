package br.com.fatec.agendabanda.controller;

import br.com.fatec.agendabanda.dto.MembroDto;
import br.com.fatec.agendabanda.dto.MembroRequest;
import br.com.fatec.agendabanda.security.TokenService;
import br.com.fatec.agendabanda.service.MembroService;
import br.com.fatec.agendabanda.service.PermissaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/membros")
public class MembroController {
    private final MembroService membroService;
    private final TokenService tokenService;
    private final PermissaoService permissaoService;

    public MembroController(MembroService membroService, TokenService tokenService, PermissaoService permissaoService) {
        this.membroService = membroService;
        this.tokenService = tokenService;
        this.permissaoService = permissaoService;
    }

    @GetMapping
    public List<MembroDto> listar() {
        return membroService.listar();
    }

    @PostMapping
    public MembroDto criar(@Valid @RequestBody MembroRequest request, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        return membroService.criar(request);
    }

    @PutMapping("/{id}")
    public MembroDto atualizar(@PathVariable Integer id, @Valid @RequestBody MembroRequest request, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        return membroService.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    public MembroDto status(@PathVariable Integer id, @RequestBody Map<String, Boolean> body, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        return membroService.alterarStatus(id, Boolean.TRUE.equals(body.get("ativo")));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> excluir(@PathVariable Integer id, HttpServletRequest http) {
        permissaoService.exigirAdmin(tokenService.sessaoObrigatoria(http));
        membroService.excluir(id);
        return Map.of("mensagem", "Membro excluído com sucesso.");
    }
}
