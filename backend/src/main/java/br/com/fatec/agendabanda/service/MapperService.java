package br.com.fatec.agendabanda.service;

import br.com.fatec.agendabanda.dto.*;
import br.com.fatec.agendabanda.model.*;
import br.com.fatec.agendabanda.repository.MusicoRepository;
import br.com.fatec.agendabanda.repository.ProdutorRepository;
import org.springframework.stereotype.Service;

@Service
public class MapperService {
    private final MusicoRepository musicoRepository;
    private final ProdutorRepository produtorRepository;

    public MapperService(MusicoRepository musicoRepository, ProdutorRepository produtorRepository) {
        this.musicoRepository = musicoRepository;
        this.produtorRepository = produtorRepository;
    }

    public MembroDto membroDto(Membro membro) {
        var musico = musicoRepository.findById(membro.getCodigo()).orElse(null);
        var produtor = produtorRepository.findById(membro.getCodigo()).orElse(null);
        String perfil = musico != null ? "MUSICO" : produtor != null ? "PRODUTOR" : "MEMBRO";
        return new MembroDto(
                membro.getCodigo(), membro.getNome(), membro.getCpf(), membro.getEmail(), membro.getLogin(),
                membro.getAdministrador(), membro.getAtivo(), perfil,
                musico != null ? musico.getInstrumento() : null,
                musico != null ? musico.getBiografia() : null,
                produtor != null ? produtor.getEspecialidade() : null
        );
    }

    public CidadeDto cidadeDto(Cidade cidade) {
        if (cidade == null) return null;
        return new CidadeDto(cidade.getCodigoCidade(), cidade.getNome(), cidade.getUf());
    }

    public LocalDto localDto(LocalEvento local) {
        if (local == null) return null;
        var cidade = local.getCidade();
        return new LocalDto(local.getCodigoLocal(), local.getNome(), local.getEndereco(), local.getComplemento(),
                cidade.getCodigoCidade(), cidade.getNome(), cidade.getUf());
    }

}
