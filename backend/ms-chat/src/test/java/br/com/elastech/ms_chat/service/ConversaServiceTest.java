package br.com.elastech.ms_chat.service;

import br.com.elastech.ms_chat.dto.request.ConversaRequest;
import br.com.elastech.ms_chat.dto.response.ConversaResponse;
import br.com.elastech.ms_chat.enums.ErrorEnum;
import br.com.elastech.ms_chat.exception.BaseException;
import br.com.elastech.ms_chat.mapper.requestMapper.ConversaRequestMapper;
import br.com.elastech.ms_chat.mapper.responseMapper.ConversaResponseMapper;
import br.com.elastech.ms_chat.model.Conversa;
import br.com.elastech.ms_chat.repository.ConversaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversaServiceTest {
    @InjectMocks
    private ConversaService service;

    @Mock
    private ConversaRepository conversaRepository;

    @Mock
    private ConversaRequestMapper conversaRequestMapper;

    @Mock
    private ConversaResponseMapper conversaResponseMapper;

    private Conversa conversa;

    @BeforeEach
    void setUp() {
        conversa = new Conversa();
        conversa.setId(1);
        conversa.setUsuario1Id(1);
        conversa.setUsuario2Id(2);
        conversa.setAtiva(true);
        conversa.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void deveCriarConversa() {

        ConversaRequest request = new ConversaRequest(1, 2);

        ConversaResponse response = ConversaResponse.builder().build();

        when(conversaRepository.findByUsuario1IdAndUsuario2Id(1, 2))
                .thenReturn(Optional.empty());

        when(conversaRequestMapper.map(request))
                .thenReturn(conversa);

        when(conversaResponseMapper.map(conversa))
                .thenReturn(response);

        ConversaResponse resultado = service.criarConversa(request);

        verify(conversaRepository).save(conversa);

        assertThat(conversa.isAtiva()).isTrue();
        assertThat(resultado).isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioForNulo() {

        ConversaRequest request =
                new ConversaRequest(null, 2);

        assertThatThrownBy(() ->
                service.criarConversa(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());

        verifyNoInteractions(conversaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioConversarConsigoMesmo() {

        ConversaRequest request =
                new ConversaRequest(1, 1);

        assertThatThrownBy(() ->
                service.criarConversa(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_INVALIDA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConversaJaExiste() {

        ConversaRequest request =
                new ConversaRequest(1, 2);

        when(conversaRepository.findByUsuario1IdAndUsuario2Id(1, 2))
                .thenReturn(Optional.of(conversa));

        assertThatThrownBy(() ->
                service.criarConversa(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_JA_EXISTE.getErrorMessage());
    }

    @Test
    void deveOrdenarOsIdsAntesDeBuscarConversa() {

        ConversaRequest request =
                new ConversaRequest(2, 1);

        when(conversaRepository.findByUsuario1IdAndUsuario2Id(1, 2))
                .thenReturn(Optional.empty());

        when(conversaRequestMapper.map(request))
                .thenReturn(conversa);

        when(conversaResponseMapper.map(conversa))
                .thenReturn(ConversaResponse.builder().build());

        service.criarConversa(request);

        verify(conversaRepository)
                .findByUsuario1IdAndUsuario2Id(1, 2);
    }

    @Test
    void deveBuscarConversa() {

        ConversaResponse response = ConversaResponse.builder().build();

        when(conversaRepository.findById(1))
                .thenReturn(Optional.of(conversa));

        when(conversaResponseMapper.map(conversa))
                .thenReturn(response);

        ConversaResponse resultado =
                service.buscarConversa(1);

        assertThat(resultado).isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoConversaNaoExiste() {

        when(conversaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.buscarConversa(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveListarConversasDoUsuario() {

        ConversaRequest request =
                new ConversaRequest(1, 2);

        ConversaResponse response = ConversaResponse.builder().build();

        when(conversaRepository
                .findByUsuario1IdOrUsuario2IdOrderByDataCriacaoDesc(1, 2))
                .thenReturn(List.of(conversa));

        when(conversaResponseMapper.map(conversa))
                .thenReturn(response);

        List<ConversaResponse> resultado =
                service.listarConversasUsuario(request);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveExcluirConversa() {

        when(conversaRepository.findById(1))
                .thenReturn(Optional.of(conversa));

        service.excluirConversa(1);

        assertThat(conversa.isAtiva()).isFalse();
    }

    @Test
    void deveLancarExcecaoAoExcluirConversaInexistente() {

        when(conversaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.excluirConversa(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONVERSA_NAO_ENCONTRADA.getErrorMessage());
    }
}
