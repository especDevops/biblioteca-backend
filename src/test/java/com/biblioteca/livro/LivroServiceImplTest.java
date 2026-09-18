package com.biblioteca.livro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceImplTest {

    @Mock
    private LivroRepository repository;

    @Mock
    private LivroMapper mapper;

    @InjectMocks
    private LivroServiceImpl service;

    @Test
    void deveCriarLivroERetornarRespostaMapeada() {
        LivroCadastroRequest request = new LivroCadastroRequest(
                "Clean Code",
                "Praticas para codigo limpo",
                "Robert C. Martin",
                2008
        );
        Livro livro = new Livro(null, request.titulo(), request.descricao(), request.autor(), request.anoPublicacao());
        Livro livroSalvo = new Livro(1L, request.titulo(), request.descricao(), request.autor(), request.anoPublicacao());
        LivroCadastroResponse resposta = new LivroCadastroResponse(
                1L,
                request.titulo(),
                request.descricao(),
                request.autor(),
                request.anoPublicacao()
        );

        when(mapper.paraEntidade(request)).thenReturn(livro);
        when(repository.save(livro)).thenReturn(livroSalvo);
        when(mapper.paraResposta(livroSalvo)).thenReturn(resposta);

        LivroCadastroResponse resultado = service.criar(request);

        assertThat(resultado).isEqualTo(resposta);
        verify(mapper).paraEntidade(request);
        verify(repository).save(livro);
        verify(mapper).paraResposta(livroSalvo);
    }
}
