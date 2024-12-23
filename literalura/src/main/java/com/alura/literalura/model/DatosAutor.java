package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutor(
    @JsonAlias("name") String nombre,
    @JsonAlias("birth_year") int añoNacimiento,
    @JsonAlias("death_year") int añoDefuncion
) {

}
