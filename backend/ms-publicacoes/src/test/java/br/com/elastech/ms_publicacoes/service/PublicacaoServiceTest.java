package br.com.elastech.ms_publicacoes.service;

import br.com.elastech.ms_publicacoes.dto.request.CriarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.dto.request.EditarPublicacaoRequest;
import br.com.elastech.ms_publicacoes.dto.response.CriarPublicacaoResponse;
import br.com.elastech.ms_publicacoes.dto.response.PublicacaoResponse;
import br.com.elastech.ms_publicacoes.enums.ErrorEnum;
import br.com.elastech.ms_publicacoes.enums.StatusPublicacao;
import br.com.elastech.ms_publicacoes.exception.BaseException;
import br.com.elastech.ms_publicacoes.mapper.requestMapper.CriarPublicacaoRequestMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.CriarPublicacaoResponseMapper;
import br.com.elastech.ms_publicacoes.mapper.responseMapper.PublicacaoResponseMapper;
import br.com.elastech.ms_publicacoes.model.Publicacao;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PublicacaoServiceTest {
    @InjectMocks
    private PublicacaoService service;

    @Mock
    private PublicacaoRepository publicacaoRepository;

    @Mock
    private CriarPublicacaoRequestMapper criarPublicacaoRequestMapper;

    @Mock
    private CriarPublicacaoResponseMapper criarPublicacaoResponseMapper;

    @Mock
    private PublicacaoResponseMapper publicacaoResponseMapper;

    private Publicacao publicacao;

    @BeforeEach
    void setUp() {
        publicacao = new Publicacao();
        publicacao.setId(1);
        publicacao.setConteudo("Conteúdo");
        publicacao.setImagem("imagem.png");
        publicacao.setStatus(StatusPublicacao.PUBLICADA);
        publicacao.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void deveCriarPublicacao() {

        CriarPublicacaoRequest request =
                new CriarPublicacaoRequest(
                        1,
                        "Conteúdo",
                        "imagem.png"
                );

        CriarPublicacaoResponse response =
                CriarPublicacaoResponse.builder().usuarioId(1).build();

        when(criarPublicacaoRequestMapper.map(request))
                .thenReturn(publicacao);

        when(criarPublicacaoResponseMapper.map(publicacao))
                .thenReturn(response);

        CriarPublicacaoResponse resultado =
                service.criarPublicacao(request);

        verify(publicacaoRepository).save(publicacao);

        assertThat(resultado).isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoConteudoForVazio() {

        CriarPublicacaoRequest request = CriarPublicacaoRequest.builder()
                .idUsuario(1)
                .conteudo("")
                .imagem(null).build();

        assertThatThrownBy(() ->
                service.criarPublicacao(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.CONTEUDO_INVALIDO.getErrorMessage());

        verifyNoInteractions(publicacaoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioForNulo() {

        CriarPublicacaoRequest request =
                new CriarPublicacaoRequest(
                        null,
                        "teste",
                        null
                );

        assertThatThrownBy(() ->
                service.criarPublicacao(request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.USUARIO_NAO_AUTORIZADO.getErrorMessage());

        verifyNoInteractions(publicacaoRepository);
    }

    @Test
    void deveBuscarPublicacaoPorId() {

        PublicacaoResponse response = PublicacaoResponse.builder().build();

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(publicacaoResponseMapper.map(publicacao))
                .thenReturn(response);

        PublicacaoResponse resultado =
                service.buscarPorId(1);

        assertThat(resultado).isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoNaoExiste() {

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.buscarPorId(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoEstiverExcluida() {

        publicacao.setStatus(StatusPublicacao.EXCLUIDA);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() ->
                service.buscarPorId(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_NAO_ENCONTRADA.getErrorMessage());
    }

    @Test
    void deveListarFeed() {

        PublicacaoResponse response = PublicacaoResponse.builder().build();

        when(publicacaoRepository.findByStatusOrderByDataCriacaoDesc(StatusPublicacao.PUBLICADA))
                .thenReturn(List.of(publicacao));

        when(publicacaoResponseMapper.map(publicacao))
                .thenReturn(response);

        List<PublicacaoResponse> resultado =
                service.listarFeed();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveListarPublicacoesDoUsuario() {

        PublicacaoResponse response = PublicacaoResponse.builder().build();

        when(publicacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(1))
                .thenReturn(List.of(publicacao));

        when(publicacaoResponseMapper.map(publicacao))
                .thenReturn(response);

        List<PublicacaoResponse> resultado =
                service.listarPorUsuario(1);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveEditarPublicacao() {

        EditarPublicacaoRequest request = EditarPublicacaoRequest.builder()
                .conteudo("Novo conteúdo")
                .imagem("nova.png")
                .build();

        PublicacaoResponse response = PublicacaoResponse.builder().build();

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        when(publicacaoResponseMapper.map(publicacao))
                .thenReturn(response);

        PublicacaoResponse resultado =
                service.editar(request, 1);

        assertThat(publicacao.getConteudo())
                .isEqualTo("Novo conteúdo");

        assertThat(publicacao.getImagem())
                .isEqualTo("nova.png");

        assertThat(publicacao.getDataAtualizacao())
                .isNotNull();

        assertThat(resultado)
                .isEqualTo(response);
    }

    @Test
    void deveLancarExcecaoAoEditarPublicacaoArquivada() {

        publicacao.setStatus(StatusPublicacao.ARQUIVADA);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        EditarPublicacaoRequest request =
                new EditarPublicacaoRequest(
                        "Novo",
                        null
                );

        assertThatThrownBy(() ->
                service.editar(request, 1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_ARQUIVADA.getErrorMessage());
    }

    @Test
    void deveArquivarPublicacao() {

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        service.arquivar(1);

        assertThat(publicacao.getStatus())
                .isEqualTo(StatusPublicacao.ARQUIVADA);
    }

    @Test
    void deveLancarExcecaoAoArquivarPublicacaoArquivada() {

        publicacao.setStatus(StatusPublicacao.ARQUIVADA);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() ->
                service.arquivar(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_ARQUIVADA.getErrorMessage());
    }

    @Test
    void deveDesarquivarPublicacao() {

        publicacao.setStatus(StatusPublicacao.ARQUIVADA);

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        service.desarquivar(1);

        assertThat(publicacao.getStatus())
                .isEqualTo(StatusPublicacao.PUBLICADA);
    }

    @Test
    void deveLancarExcecaoQuandoPublicacaoJaEstiverPublicada() {

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() ->
                service.desarquivar(1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorEnum.PUBLICACAO_PUBLICADA.getErrorMessage());
    }

    @Test
    void deveExcluirPublicacao() {

        when(publicacaoRepository.findById(1))
                .thenReturn(Optional.of(publicacao));

        service.excluir(1);

        assertThat(publicacao.getStatus())
                .isEqualTo(StatusPublicacao.EXCLUIDA);
    }
}
