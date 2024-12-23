package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")

    private String nombre;

    @JsonAlias("birth_year")

    private int añoNacimiento;

    @JsonAlias("death_year")
    private int añoDefuncion;

    @ManyToMany(mappedBy = "autores",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros=new ArrayList<>();

    // Constructor, getters y setters
    public Autor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAñoNacimiento() {
        return añoNacimiento;
    }

    public void setAñoNacimiento(int añoNacimiento) {
        this.añoNacimiento = añoNacimiento;
    }

    public int getAñoDefuncion() {
        return añoDefuncion;
    }

    public void setAñoDefuncion(int añoDefuncion) {
        this.añoDefuncion = añoDefuncion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
