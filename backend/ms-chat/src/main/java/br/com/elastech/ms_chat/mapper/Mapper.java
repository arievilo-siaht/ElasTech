package br.com.elastech.ms_chat.mapper;

public interface Mapper <S, T> {
    T map(S source);
}

