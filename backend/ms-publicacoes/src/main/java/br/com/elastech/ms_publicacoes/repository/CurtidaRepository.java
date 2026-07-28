package br.com.elastech.ms_publicacoes.repository;

import br.com.elastech.ms_publicacoes.model.Curtida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurtidaRepository extends JpaRepository<Curtida, Integer> {
    Optional<Curtida> findByPublicacaoIdAndUsuarioId(Integer publicacaoId, Integer usuarioId);

    List<Curtida> findByPublicacaoIdAndAtivoTrue(Integer publicacaoId);

    boolean existsByPublicacaoIdAndUsuarioIdAndAtivoTrue(Integer publicacaoId, Integer usuarioId);
}
