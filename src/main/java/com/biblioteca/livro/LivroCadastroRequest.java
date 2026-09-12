package com.biblioteca.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroCadastroRequest(
        @NotBlank
        String titulo,
        @NotBlank
        String descricao,
        @NotBlank
        String autor,
        @NotNull
        Integer anoPublicacao
) {
}
