package com.biblioteca.livro;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro paraEntidade(LivroCadastroRequest request);

    LivroCadastroResponse paraResposta(Livro livro);

    void atualizarEntidade(LivroCadastroRequest request, @MappingTarget Livro livro);
}
