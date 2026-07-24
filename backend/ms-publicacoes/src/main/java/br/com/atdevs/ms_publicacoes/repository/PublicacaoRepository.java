package br.com.atdevs.ms_publicacoes.repository;

import br.com.atdevs.ms_publicacoes.model.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacaoRepository extends JpaRepository<Publicacao, Integer> {
    List<Publicacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);
}
