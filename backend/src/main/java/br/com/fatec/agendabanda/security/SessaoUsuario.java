package br.com.fatec.agendabanda.security;

public record SessaoUsuario(Integer codigo, String nome, String login, boolean administrador) {
}
