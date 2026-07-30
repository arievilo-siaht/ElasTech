package br.com.elastech.ms_chat.repository;

import br.com.elastech.ms_chat.enums.StatusMensagem;
import br.com.elastech.ms_chat.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {
    List<Mensagem> findByConversaIdAndStatusMensagemNotOrderByDataEnvioAsc(
            Integer conversaId,
            StatusMensagem statusMensagem
    );
}
