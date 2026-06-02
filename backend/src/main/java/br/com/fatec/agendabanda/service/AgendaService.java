package br.com.fatec.agendabanda.service;

import br.com.fatec.agendabanda.dto.AgendaDto;
import br.com.fatec.agendabanda.dto.AgendaRequest;
import br.com.fatec.agendabanda.model.*;
import br.com.fatec.agendabanda.repository.*;
import br.com.fatec.agendabanda.security.SessaoUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final EnsaioRepository ensaioRepository;
    private final ReuniaoRepository reuniaoRepository;
    private final ShowEventoRepository showRepository;
    private final EnsaioMusicoRepository ensaioMusicoRepository;
    private final MusicoRepository musicoRepository;
    private final MembroService membroService;
    private final LocalService localService;
    private final CidadeRepository cidadeRepository;
    private final MapperService mapper;
    private final PermissaoService permissaoService;

    public AgendaService(AgendaRepository agendaRepository, EnsaioRepository ensaioRepository, ReuniaoRepository reuniaoRepository,
                         ShowEventoRepository showRepository, EnsaioMusicoRepository ensaioMusicoRepository, MusicoRepository musicoRepository,
                         MembroService membroService, LocalService localService,
                         CidadeRepository cidadeRepository, MapperService mapper, PermissaoService permissaoService) {
        this.agendaRepository = agendaRepository;
        this.ensaioRepository = ensaioRepository;
        this.reuniaoRepository = reuniaoRepository;
        this.showRepository = showRepository;
        this.ensaioMusicoRepository = ensaioMusicoRepository;
        this.musicoRepository = musicoRepository;
        this.membroService = membroService;
        this.localService = localService;
        this.cidadeRepository = cidadeRepository;
        this.mapper = mapper;
        this.permissaoService = permissaoService;
    }

    @Transactional(readOnly = true)
    public List<AgendaDto> listar() {
        return agendaRepository.findAllByOrderByDataHoraInicioAsc().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AgendaDto detalhar(Integer id) {
        return toDto(buscar(id));
    }

    public Agenda buscar(Integer id) {
        return agendaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento não encontrado."));
    }

    @Transactional
    public AgendaDto criar(AgendaRequest request, SessaoUsuario sessao) {
        validarPeriodo(request);
        var agenda = new Agenda();
        aplicarDados(agenda, request);
        agenda.setCriador(membroService.buscarEntidade(sessao.codigo()));
        agenda.setCriadoEm(LocalDateTime.now());
        var salvo = agendaRepository.save(agenda);
        salvarDetalhes(salvo, request);
        return toDto(salvo);
    }

    @Transactional
    public AgendaDto atualizar(Integer id, AgendaRequest request, SessaoUsuario sessao) {
        validarPeriodo(request);
        var agenda = buscar(id);
        permissaoService.exigirAdminOuCriador(sessao, agenda);
        limparDetalhes(id);
        aplicarDados(agenda, request);
        agenda.setAtualizadoEm(LocalDateTime.now());
        var salvo = agendaRepository.save(agenda);
        salvarDetalhes(salvo, request);
        return toDto(salvo);
    }

    @Transactional
    public void excluir(Integer id, SessaoUsuario sessao) {
        var agenda = buscar(id);
        permissaoService.exigirAdminOuCriador(sessao, agenda);
        limparDetalhes(id);
        agendaRepository.delete(agenda);
    }

    private void aplicarDados(Agenda agenda, AgendaRequest request) {
        agenda.setTitulo(request.titulo());
        agenda.setDescricao(request.descricao());
        agenda.setDataHoraInicio(request.dataHoraInicio());
        agenda.setDataHoraFim(request.dataHoraFim());
        agenda.setLocal(localService.buscarLocal(request.localId()));
    }

    private void salvarDetalhes(Agenda agenda, AgendaRequest request) {
        String tipo = request.tipoEspecifico().toUpperCase();
        if ("ENSAIO".equals(tipo)) {
            var ensaio = new Ensaio();
            ensaio.setAgenda(agenda);
            ensaio.setRepertorio(request.repertorio() == null || request.repertorio().isBlank() ? "Repertório não informado" : request.repertorio());
            ensaio.setObservacao(request.observacao());
            ensaioRepository.save(ensaio);
            if (request.musicosIds() != null) {
                for (Integer musicoId : request.musicosIds()) {
                    var musico = musicoRepository.findById(musicoId).orElseThrow(() -> new IllegalArgumentException("Músico não encontrado."));
                    var item = new EnsaioMusico();
                    item.setId(new EnsaioMusicoId(agenda.getCodigoAgenda(), musico.getCodigo()));
                    item.setEnsaio(ensaio);
                    item.setMusico(musico);
                    ensaioMusicoRepository.save(item);
                }
            }
            return;
        }
        if ("SHOW".equals(tipo)) {
            var show = new ShowEvento();
            show.setAgenda(agenda);
            var cidade = cidadeRepository.findById(request.cidadeId()).orElseThrow(() -> new IllegalArgumentException("Cidade do show não encontrada."));
            show.setCidade(cidade);
            showRepository.save(show);
            return;
        }
        if ("REUNIAO".equals(tipo) || "REUNIÃO".equals(tipo)) {
            var reuniao = new Reuniao();
            reuniao.setAgenda(agenda);
            reuniao.setPauta(request.pauta() == null || request.pauta().isBlank() ? "Pauta não informada" : request.pauta());
            reuniaoRepository.save(reuniao);
            return;
        }
        throw new IllegalArgumentException("Tipo específico deve ser ENSAIO, SHOW ou REUNIAO.");
    }

    private void limparDetalhes(Integer id) {
        ensaioMusicoRepository.deleteByEnsaioCodigoAgenda(id);
        ensaioRepository.deleteById(id);
        reuniaoRepository.deleteById(id);
        showRepository.deleteById(id);
    }

    private void validarPeriodo(AgendaRequest request) {
        if (!request.dataHoraFim().isAfter(request.dataHoraInicio())) {
            throw new IllegalArgumentException("A data/hora final deve ser maior que a inicial.");
        }
    }

    private AgendaDto toDto(Agenda agenda) {
        var ensaio = ensaioRepository.findById(agenda.getCodigoAgenda()).orElse(null);
        var reuniao = reuniaoRepository.findById(agenda.getCodigoAgenda()).orElse(null);
        var show = showRepository.findById(agenda.getCodigoAgenda()).orElse(null);
        String tipoEspecifico = ensaio != null ? "ENSAIO" : show != null ? "SHOW" : reuniao != null ? "REUNIAO" : "EVENTO";
        var musicos = ensaioMusicoRepository.findByEnsaioCodigoAgenda(agenda.getCodigoAgenda()).stream()
                .map(item -> mapper.membroDto(item.getMusico().getMembro()))
                .toList();

        return new AgendaDto(
                agenda.getCodigoAgenda(), agenda.getTitulo(), agenda.getDescricao(), agenda.getDataHoraInicio(), agenda.getDataHoraFim(),
                mapper.localDto(agenda.getLocal()), mapper.membroDto(agenda.getCriador()),
                tipoEspecifico,
                ensaio != null ? ensaio.getRepertorio() : null,
                ensaio != null ? ensaio.getObservacao() : null,
                reuniao != null ? reuniao.getPauta() : null,
                show != null ? mapper.cidadeDto(show.getCidade()) : null,
                musicos
        );
    }
}
