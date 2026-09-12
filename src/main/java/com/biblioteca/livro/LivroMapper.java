package com.biblioteca.livro;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro paraEntidade(LivroCadastroRequest request);

    LivroCadastroResponse paraResposta(Livro livro);
}
