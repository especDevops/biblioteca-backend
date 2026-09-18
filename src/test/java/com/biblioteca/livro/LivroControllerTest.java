package com.biblioteca.livro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroControllerTest {

    @Mock
    private LivroService service;

    @InjectMocks
    private LivroController controller;

    @Test
    void deveRetornarLivroCriadoComStatus201() {
        LivroCadastroRequest request = new LivroCadastroRequest(
                "Domain-Driven Design",
                "Modelagem de dominios complexos",
                "Eric Evans",
                2003
        );
        LivroCadastroResponse resposta = new LivroCadastroResponse(
                1L,
                request.titulo(),
                request.descricao(),
                request.autor(),
                request.anoPublicacao()
        );
        when(service.criar(request)).thenReturn(resposta);

        ResponseEntity<LivroCadastroResponse> resultado = controller.criar(request);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resultado.getBody()).isEqualTo(resposta);
        verify(service).criar(request);
    }
}
