package br.com.elastech.ms_publicacoes.mapper;

public interface Mapper <S, T> {
    T map(S source);
}
