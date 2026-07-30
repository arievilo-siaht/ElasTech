package br.com.elastech.ms_chat.service;

import br.com.elastech.ms_chat.dto.request.MensagemRequest;
import br.com.elastech.ms_chat.dto.response.MensagemResponse;
import br.com.elastech.ms_chat.enums.ErrorEnum;
import br.com.elastech.ms_chat.enums.StatusMensagem;
import br.com.elastech.ms_chat.exception.BaseException;
import br.com.elastech.ms_chat.mapper.requestMapper.MensagemRequestMapper;
import br.com.elastech.ms_chat.mapper.responseMapper.MensagemResponseMapper;
import br.com.elastech.ms_chat.model.Conversa;
import br.com.elastech.ms_chat.model.Mensagem;
import br.com.elastech.ms_chat.repository.ConversaRepository;
import br.com.elastech.ms_chat.repository.MensagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MensagemServiceTest {
    @InjectMocks
    private MensagemService service;

    @Mock
    private MensagemRepository mensagemRepository;

    @Mock
    private ConversaRepository conversaRepository;

    @Mock
    private MensagemRequestMapper mensagemRequestMapper;

    @Mock
    private MensagemResponseMapper mensagemResponseMapper;

    private Conversa conversa;
    private Mensagem mensagem;

    @BeforeEach
    void setUp() {

        conversa = new Conversa();
        conversa.setId(1);
        conversa.setUsuario1Id(1);
        conversa.setUsuario2Id(2);
        conversa.setAtiva(true);

        mensagem = new Mensagem();
        mensagem.setId(1);
        mensagem.setRemetenteId(1);
        mensagem.setDestinatarioId(2);
        mensagem.setConteudo("Olá");
        mensagem.setStatusMensagem(StatusMensagem.ENVIADA);
        mensagem.setConversa(conversa);
    }

    @Test
    void deveEnviarMensagem() {

        MensagemRequest request =
                new MensagemRequest(
                        1,
                        1,
                        2,
                        "Olá"
                );

        MensagemResponse response = MensagemResponse.builder().build();

        when(conversaRepository.findByUsuario1IdAndUsuario2Id(1, 2))
                .thenReturn(Optional.of(conversa));

        when(mensagemRequestMapper.map(request))
                .thenReturn(mensagem);

        when(mensagemResponseMapper.map(mensagem))
                .thenReturn(response);

        MensagemResponse resultado =
                service.enviarMensagem(request);

        verify(mensagemRepository).save(mensagem);

        assertThat(mensagem.getConversa())
                .isEqualTo(conversa);

        assertThat(mensagem.getStatusMensagem())
                .isEqualTo(StatusMensagem.ENVIADA);

        assertThat(resultado)
                .isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoRemetenteForNulo() {

        MensagemRequest request =
                new MensagemRequest(
                        null,
                        null,
                        2,
                        "Olá"
                );

        assertThatThrownBy(() ->
                service.enviarMensagem(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());

        verifyNoInteractions(conversaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoEnviarMensagemParaSiMesmo() {

        MensagemRequest request =
                new MensagemRequest(
                        null,
                        1,
                        1,
                        "Olá"
                );

        assertThatThrownBy(() ->
                service.enviarMensagem(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_INVALIDA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConteudoForVazio() {

        MensagemRequest request =
                new MensagemRequest(
                        1,
                        1,
                        2,
                        ""
                );

        assertThatThrownBy(() ->
                service.enviarMensagem(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONTEUDO_INVALIDO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConversaNaoExiste() {

        MensagemRequest request =
                new MensagemRequest(
                        1,
                        1,
                        2,
                        "Olá"
                );

        when(conversaRepository.findByUsuario1IdAndUsuario2Id(1, 2))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.enviarMensagem(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConversaEstiverEncerrada() {

        conversa.setAtiva(false);

        MensagemRequest request =
                new MensagemRequest(
                        1,
                        1,
                        2,
                        "Olá"
                );

        when(conversaRepository.findByUsuario1IdAndUsuario2Id(1, 2))
                .thenReturn(Optional.of(conversa));

        assertThatThrownBy(() ->
                service.enviarMensagem(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_ENCERRADA.getErrorMessage());
    }

    @Test
    void deveListarMensagensDaConversa() {

        MensagemResponse response = MensagemResponse.builder().build();

        when(conversaRepository.findById(1))
                .thenReturn(Optional.of(conversa));

        when(mensagemRepository
                .findByConversaIdAndStatusMensagemNotOrderByDataEnvioAsc(
                        1,
                        StatusMensagem.EXCLUIDA))
                .thenReturn(List.of(mensagem));

        when(mensagemResponseMapper.map(mensagem))
                .thenReturn(response);

        List<MensagemResponse> resultado =
                service.listarMensagens(1);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveLancarExcecaoAoListarMensagensDeConversaInexistente() {

        when(conversaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.listarMensagens(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveMarcarMensagemComoLida() {

        MensagemResponse response = MensagemResponse.builder().build();

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        when(mensagemResponseMapper.map(mensagem))
                .thenReturn(response);

        MensagemResponse resultado =
                service.marcarComoLida(1, 2);

        assertThat(mensagem.getStatusMensagem())
                .isEqualTo(StatusMensagem.LIDA);

        assertThat(resultado)
                .isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDestinatario() {

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() ->
                service.marcarComoLida(1, 99))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoMensagemEstiverExcluidaAoMarcarComoLida() {

        mensagem.setStatusMensagem(StatusMensagem.EXCLUIDA);

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() ->
                service.marcarComoLida(1, 2))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.MENSAGEM_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveEditarMensagem() {

        MensagemRequest request =
                new MensagemRequest(
                        null,
                        1,
                        2,
                        "Novo conteúdo"
                );

        MensagemResponse response = MensagemResponse.builder().build();

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        when(mensagemResponseMapper.map(mensagem))
                .thenReturn(response);

        MensagemResponse resultado =
                service.editarMensagem(request, 1);

        assertThat(mensagem.getConteudo())
                .isEqualTo("Novo conteúdo");

        assertThat(mensagem.getStatusMensagem())
                .isEqualTo(StatusMensagem.EDITADA);

        assertThat(resultado)
                .isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForRemetente() {

        MensagemRequest request =
                new MensagemRequest(
                        null,
                        2,
                        1,
                        "Novo"
                );

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() ->
                service.editarMensagem(request,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEditarMensagemExcluida() {

        mensagem.setStatusMensagem(StatusMensagem.EXCLUIDA);

        MensagemRequest request =
                new MensagemRequest(
                        1,
                        1,
                        2,
                        "Olá"
                );

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() ->
                service.editarMensagem(request,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.MENSAGEM_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveExcluirMensagem() {

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        service.excluirMensagem(1,1);

        assertThat(mensagem.getStatusMensagem())
                .isEqualTo(StatusMensagem.EXCLUIDA);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForRemetenteAoExcluir() {

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() ->
                service.excluirMensagem(1,99))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoMensagemJaEstiverExcluida() {

        mensagem.setStatusMensagem(StatusMensagem.EXCLUIDA);

        when(mensagemRepository.findById(1))
                .thenReturn(Optional.of(mensagem));

        assertThatThrownBy(() ->
                service.excluirMensagem(1,1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.MENSAGEM_NAO_ENCONTRADA.getErrorMessage());
    }
}
