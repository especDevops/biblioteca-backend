package com.biblioteca.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroCadastroResponse(
        Long id,
        String titulo,
        String descricao,
        String autor,
        Integer anoPublicacao
) {
}
