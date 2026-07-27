package br.com.elastech.ms_publicacoes.repository;

import br.com.elastech.ms_publicacoes.model.Comentario;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
}
