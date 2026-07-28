package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CurtidaRequest;
import br.com.elastech.ms_publicacoes.dto.response.CurtidaResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.mapper.requestMapper.CurtidaRequestMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.CurtidaResponseMapper;
import br.com.elastech.ms_publicacoes.model.Curtida;
import br.com.elastech.ms_publicacoes.model.Publicacao;
import br.com.elastech.ms_publicacoes.repository.CurtidaRepository;
import br.com.elastech.ms_publicacoes.repository.PublicacaoRepository;
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
public class CurtidaServiceTest {
    @InjectMocks
    private CurtidaService service;

    @Mock
    private CurtidaRepository curtidaRepository;

    @Mock
    private PublicacaoRepository publicacaoRepository;

    @Mock
    private CurtidaRequestMapper requestMapper;

    @Mock
    private CurtidaResponseMapper responseMapper;

    private Publicacao publicacao;
    private Curtida curtida;

    @BeforeEach
    void setUp() {
        publicacao = new Publicacao();
        publicacao.setId(1);
        publicacao.setStatus(StatusPublicacao.PUBLICADA);

        curtida = new Curtida();
        curtida.setId(1);
        curtida.setUsuarioId(1);
        curtida.setPublicacao(publicacao);
        curtida.setAtivo(true);
        curtida.setDataCurtida(LocalDateTime.now());
    }

    @Test
    void deveCriarNovaCurtida() {

        CurtidaRequest request = new CurtidaRequest(1, 1);

        CurtidaResponse response = CurtidaResponse.builder().build();

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(curtidaRepository.findByPublicacaoIdAndUsuarioId(1, 1))
                .thenReturn(Optional.empty());

        when(requestMapper.map(request))
                .thenReturn(curtida);

        when(responseMapper.map(curtida))
                .thenReturn(response);

        CurtidaResponse resultado = service.curtir(request);

        verify(curtidaRepository).save(curtida);

        assertThat(resultado).isEqualTo(response);
    }

    @Test
    void deveReativarCurtida() {

        curtida.setAtivo(false);

        CurtidaRequest request = new CurtidaRequest(1, 1);

        CurtidaResponse response = CurtidaResponse.builder().build();

                when(publicacaoRepository.findById(1))
                        .thenReturn(Optional.of(publicacao));

        when(curtidaRepository.findByPublicacaoIdAndUsuarioId(1, 1))
                .thenReturn(Optional.of(curtida));

        when(responseMapper.map(curtida))
                .thenReturn(response);

        CurtidaResponse resultado = service.curtir(request);

        assertThat(curtida.isAtivo()).isTrue();
        assertThat(curtida.getDataCurtida()).isNotNull();
        assertThat(resultado).isEqualTo(response);

        verify(curtidaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoJaEstiverCurtida() {

        CurtidaRequest request = new CurtidaRequest(1,1);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(curtidaRepository.findByPublicacaoIdAndUsuarioId(1,1))
                .thenReturn(Optional.of(curtida));

        assertThatThrownBy(() -> service.curtir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_JA_CURTIDA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioForNulo() {

        CurtidaRequest request = new CurtidaRequest(1,null);

        assertThatThrownBy(() -> service.curtir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());

        verifyNoInteractions(publicacaoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoNaoExiste() {

        CurtidaRequest request = new CurtidaRequest(1,1);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.curtir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoEstiverExcluida() {

        publicacao.setStatus(StatusPublicacao.EXCLUIDA);

        CurtidaRequest request = new CurtidaRequest(1,1);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() -> service.curtir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveDescurtirPublicacao() {

        CurtidaRequest request = new CurtidaRequest(1,1);

        when(curtidaRepository.findByPublicacaoIdAndUsuarioId(1,1))
                .thenReturn(Optional.of(curtida));

        service.descurtir(request);

        assertThat(curtida.isAtivo()).isFalse();
    }

    @Test
    void deveLancarExcecaoQuandoCurtidaNaoExiste() {

        CurtidaRequest request = new CurtidaRequest(1,1);

        when(curtidaRepository.findByPublicacaoIdAndUsuarioId(1,1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.descurtir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CURTIDA_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCurtidaJaEstiverInativa() {

        curtida.setAtivo(false);

        CurtidaRequest request = new CurtidaRequest(1,1);

        when(curtidaRepository.findByPublicacaoIdAndUsuarioId(1,1))
                .thenReturn(Optional.of(curtida));

        assertThatThrownBy(() -> service.descurtir(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CURTIDA_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveListarCurtidasDaPublicacao() {

        CurtidaResponse response = CurtidaResponse.builder().build();

                when(publicacaoRepository.findById(1))
                        .thenReturn(Optional.of(publicacao));

        when(curtidaRepository.findByPublicacaoIdAndAtivoTrue(1))
                .thenReturn(List.of(curtida));

        when(responseMapper.map(curtida))
                .thenReturn(response);

        List<CurtidaResponse> resultado = service.listarCurtidas(1);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveLancarExcecaoAoListarCurtidasDePublicacaoInexistente() {

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.listarCurtidas(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveRetornarTrueQuandoUsuarioJaCurtiu() {

        when(curtidaRepository.existsByPublicacaoIdAndUsuarioIdAndAtivoTrue(1,1))
                .thenReturn(true);

        boolean resultado = service.usuarioJaCurtiu(1,1);

        assertThat(resultado).isTrue();
    }

    @Test
    void deveRetornarFalseQuandoUsuarioNaoCurtiu() {

        when(curtidaRepository.existsByPublicacaoIdAndUsuarioIdAndAtivoTrue(1,1))
                .thenReturn(false);

        boolean resultado = service.usuarioJaCurtiu(1,1);

        assertThat(resultado).isFalse();
    }
}
