package br.com.fatec.agendabanda.service;

import br.com.fatec.agendabanda.dto.CidadeDto;
import br.com.fatec.agendabanda.dto.LocalDto;
import br.com.fatec.agendabanda.dto.LocalRequest;
import br.com.fatec.agendabanda.model.LocalEvento;
import br.com.fatec.agendabanda.repository.CidadeRepository;
import br.com.fatec.agendabanda.repository.LocalEventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocalService {
    private final LocalEventoRepository localRepository;
    private final CidadeRepository cidadeRepository;
    private final MapperService mapper;

    public LocalService(LocalEventoRepository localRepository, CidadeRepository cidadeRepository, MapperService mapper) {
        this.localRepository = localRepository;
        this.cidadeRepository = cidadeRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<LocalDto> listarLocais() {
        return localRepository.findAll().stream().map(mapper::localDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CidadeDto> listarCidades() {
        return cidadeRepository.findAll().stream().map(mapper::cidadeDto).toList();
    }

    public LocalEvento buscarLocal(Integer id) {
        return localRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Local não encontrado."));
    }

    public LocalDto criar(LocalRequest request) {
        var local = new LocalEvento();
        aplicar(local, request);
        return mapper.localDto(localRepository.save(local));
    }

    public LocalDto atualizar(Integer id, LocalRequest request) {
        var local = buscarLocal(id);
        aplicar(local, request);
        return mapper.localDto(localRepository.save(local));
    }

    public void excluir(Integer id) {
        localRepository.deleteById(id);
    }

    private void aplicar(LocalEvento local, LocalRequest request) {
        var cidade = cidadeRepository.findById(request.cidadeId()).orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada."));
        local.setNome(request.nome());
        local.setEndereco(request.endereco());
        local.setComplemento(request.complemento());
        local.setCidade(cidade);
    }
}
