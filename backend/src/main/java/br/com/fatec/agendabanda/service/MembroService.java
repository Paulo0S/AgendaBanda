package br.com.fatec.agendabanda.service;

import br.com.fatec.agendabanda.dto.MembroDto;
import br.com.fatec.agendabanda.dto.MembroRequest;
import br.com.fatec.agendabanda.model.Membro;
import br.com.fatec.agendabanda.model.Musico;
import br.com.fatec.agendabanda.model.Produtor;
import br.com.fatec.agendabanda.repository.MembroRepository;
import br.com.fatec.agendabanda.repository.MusicoRepository;
import br.com.fatec.agendabanda.repository.ProdutorRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MembroService {
    private final MembroRepository membroRepository;
    private final MusicoRepository musicoRepository;
    private final ProdutorRepository produtorRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MapperService mapperService;

    public MembroService(MembroRepository membroRepository, MusicoRepository musicoRepository, ProdutorRepository produtorRepository,
                         BCryptPasswordEncoder passwordEncoder, MapperService mapperService) {
        this.membroRepository = membroRepository;
        this.musicoRepository = musicoRepository;
        this.produtorRepository = produtorRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapperService = mapperService;
    }

    @Transactional(readOnly = true)
    public List<MembroDto> listar() {
        return membroRepository.findAll().stream().map(mapperService::membroDto).toList();
    }

    public Membro buscarEntidade(Integer id) {
        return membroRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Membro não encontrado."));
    }

    @Transactional
    public MembroDto criar(MembroRequest request) {
        if (membroRepository.existsByLogin(request.login())) throw new IllegalArgumentException("Login já cadastrado.");
        if (membroRepository.existsByEmail(request.email())) throw new IllegalArgumentException("E-mail já cadastrado.");
        if (membroRepository.existsByCpf(request.cpf())) throw new IllegalArgumentException("CPF já cadastrado.");
        if (request.senha() == null || request.senha().isBlank()) throw new IllegalArgumentException("Senha é obrigatória no cadastro.");

        var membro = new Membro();
        aplicarDados(membro, request);
        membro.setSenha(passwordEncoder.encode(request.senha()));
        membro.setCriadoEm(LocalDateTime.now());
        var salvo = membroRepository.save(membro);
        salvarPerfil(salvo, request);
        return mapperService.membroDto(salvo);
    }

    @Transactional
    public MembroDto atualizar(Integer id, MembroRequest request) {
        var membro = buscarEntidade(id);
        aplicarDados(membro, request);
        if (request.senha() != null && !request.senha().isBlank()) {
            membro.setSenha(passwordEncoder.encode(request.senha()));
        }
        var salvo = membroRepository.save(membro);
        musicoRepository.deleteById(id);
        produtorRepository.deleteById(id);
        salvarPerfil(salvo, request);
        return mapperService.membroDto(salvo);
    }

    @Transactional
    public MembroDto alterarStatus(Integer id, boolean ativo) {
        var membro = buscarEntidade(id);
        membro.setAtivo(ativo);
        return mapperService.membroDto(membroRepository.save(membro));
    }

    @Transactional
    public void excluir(Integer id) {
        musicoRepository.deleteById(id);
        produtorRepository.deleteById(id);
        membroRepository.deleteById(id);
    }

    private void aplicarDados(Membro membro, MembroRequest request) {
        membro.setNome(request.nome());
        membro.setCpf(request.cpf());
        membro.setEmail(request.email());
        membro.setLogin(request.login());
        membro.setAdministrador(Boolean.TRUE.equals(request.administrador()));
        membro.setAtivo(request.ativo() == null || request.ativo());
    }

    private void salvarPerfil(Membro membro, MembroRequest request) {
        String perfil = request.perfil() == null ? "MEMBRO" : request.perfil().toUpperCase();
        if ("MUSICO".equals(perfil)) {
            var musico = new Musico();
            musico.setMembro(membro);
            musico.setInstrumento(request.instrumento() == null || request.instrumento().isBlank() ? "Não informado" : request.instrumento());
            musico.setBiografia(request.biografia());
            musicoRepository.save(musico);
        }
        if ("PRODUTOR".equals(perfil)) {
            var produtor = new Produtor();
            produtor.setMembro(membro);
            produtor.setEspecialidade(request.especialidade() == null || request.especialidade().isBlank() ? "Não informado" : request.especialidade());
            produtorRepository.save(produtor);
        }
    }
}
