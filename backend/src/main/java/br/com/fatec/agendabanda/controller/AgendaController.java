package br.com.fatec.agendabanda.controller;

import br.com.fatec.agendabanda.dto.AgendaDto;
import br.com.fatec.agendabanda.dto.AgendaRequest;
import br.com.fatec.agendabanda.security.TokenService;
import br.com.fatec.agendabanda.service.AgendaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {
    private final AgendaService service;
    private final TokenService tokenService;

    public AgendaController(AgendaService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public List<AgendaDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public AgendaDto detalhar(@PathVariable Integer id) {
        return service.detalhar(id);
    }

    @PostMapping
    public AgendaDto criar(@Valid @RequestBody AgendaRequest request, HttpServletRequest http) {
        return service.criar(request, tokenService.sessaoObrigatoria(http));
    }

    @PutMapping("/{id}")
    public AgendaDto atualizar(@PathVariable Integer id, @Valid @RequestBody AgendaRequest request, HttpServletRequest http) {
        return service.atualizar(id, request, tokenService.sessaoObrigatoria(http));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> excluir(@PathVariable Integer id, HttpServletRequest http) {
        service.excluir(id, tokenService.sessaoObrigatoria(http));
        return Map.of("mensagem", "Evento excluído com sucesso.");
    }
}
