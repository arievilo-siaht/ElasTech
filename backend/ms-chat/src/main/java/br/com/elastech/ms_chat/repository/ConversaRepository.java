package br.com.elastech.ms_chat.repository;

import br.com.elastech.ms_chat.model.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Integer> {
    Optional<Conversa> findByUsuario1IdAndUsuario2Id(
            Integer usuario1Id,
            Integer usuario2Id
    );

    List<Conversa> findByUsuario1IdOrUsuario2IdOrderByDataCriacaoDesc(
            Integer usuario1Id,
            Integer usuario2Id
    );

}
