package br.com.fatec.agendabanda.service;

import br.com.fatec.agendabanda.model.Agenda;
import br.com.fatec.agendabanda.security.SessaoUsuario;
import org.springframework.stereotype.Service;

@Service
public class PermissaoService {
    public void exigirAdmin(SessaoUsuario sessao) {
        if (!sessao.administrador()) {
            throw new SecurityException("Apenas administradores podem executar esta ação.");
        }
    }

    public void exigirAdminOuCriador(SessaoUsuario sessao, Agenda agenda) {
        boolean criador = agenda.getCriador().getCodigo().equals(sessao.codigo());
        if (!sessao.administrador() && !criador) {
            throw new SecurityException("Apenas o administrador ou o criador do evento pode executar esta ação.");
        }
    }
}
