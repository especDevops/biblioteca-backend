package com.biblioteca.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroCadastroRequest(
        @NotBlank
        String titulo,
        String descricao,
        @NotBlank
        String autor,
        @NotBlank
        String genero,
        @NotNull
        Integer anoPublicacao
) {
}
