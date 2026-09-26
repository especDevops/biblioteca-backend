package com.biblioteca.livro;

import jakarta.validation.constraints.Pattern;

public record LivroAtualizacaoRequest(
        @Pattern(regexp = ".*\\S.*", message = "não pode estar em branco")
        String titulo,
        @Pattern(regexp = ".*\\S.*", message = "não pode estar em branco")
        String descricao,
        @Pattern(regexp = ".*\\S.*", message = "não pode estar em branco")
        String autor,
        Integer anoPublicacao
) {
}
