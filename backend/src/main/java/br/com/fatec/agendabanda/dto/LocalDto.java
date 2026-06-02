package br.com.fatec.agendabanda.dto;

public record LocalDto(Integer codigoLocal, String nome, String endereco, String complemento, Integer cidadeId, String cidadeNome, String uf) {
}
