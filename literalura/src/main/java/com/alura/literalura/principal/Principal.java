package com.alura.literalura.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repositorio.LibroRepository;
import com.alura.literalura.repositorio.AutorRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

public class Principal {
    private String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Scanner teclado = new Scanner(System.in);

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu() {
        var menu = """
                 -------------------------------
                 1. Buscar Libro
                 2. Buscar Autor
                 3. Ver libros buscados
                 4. Buscar libros por idioma
                 5. Buscar autores vivos por año

                 0. Salir
                 -------------------------------

                """;

        var opc = 1;
        while (opc != 0) {
            System.out.println(menu);
            opc = teclado.nextInt();
            teclado.nextLine();

            switch (opc) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    buscarAutor();
                    break;
                case 3:
                    System.out.println("Metodo aun no implementado");
                    break;
                case 4:
                    System.out.println("Metodo aun no implementado");
                    break;
                case 5:
                    System.out.println("Metodo aun no implementado");
                    break;
                case 0:
                    System.out.println("\nGracias por utilizar el programa\nCerrando aplicacion...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
                    break;
            }
        }
    }

    public Libro getDatosLibro() {
        System.out.println("\nQue libro deseas buscar?\n");
        var libroBuscado = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + libroBuscado.replace(" ", "%20"));
        DatosLibro datos = convierteDatos.obtenerDatos(json, DatosLibro.class);
        Optional<Libro> primerLibro = datos.libros().stream().findFirst();
        return primerLibro.orElse(null);
    }

    public void buscarLibro() {
        Libro libro = getDatosLibro();

        if (libro != null) {
            configurarRelacionesLibro(libro);
            imprimirLibro(libro);
            libroRepository.save(libro);
        } else {
            System.out.println("\nLibro no encontrado");
        }
    }

    public DatosLibro datosLibros() {
        System.out.println("Ingresa el nombre y apellido del autor (separados por un espacio):\n");
        var autorBuscado = teclado.nextLine();
        var partes = autorBuscado.split("\\s+");

        if (partes.length < 2) {
            System.out.println("Por favor, ingresa tanto el nombre como el apellido.");
            return null;
        }

        var nombre = partes[0];
        var apellido = partes[1];

        var url = String.format("%s%s%%20%s", URL_BASE, apellido, nombre);
        var json = consumoApi.obtenerDatos(url);
        DatosLibro datos = convierteDatos.obtenerDatos(json, DatosLibro.class);
        return datos;
    }

    public void buscarAutor() {
        DatosLibro datosLibro = datosLibros();
        if (datosLibro != null) {
            List<Libro> libros = datosLibro.libros();
            if (libros.size() > 0) {
                System.out.println("\nLibros encontrados:");
                for (Libro libro : libros) {
                    configurarRelacionesLibro(libro);
                }
                imprimirLibros(libros);
                libroRepository.saveAll(libros);
            } else {
                System.out.println("\nAutor no encontrado");
            }
        }
    }

    public void configurarRelacionesLibro(Libro libro) {
        List<Autor> autores = new ArrayList<>();
        for (Autor autor : libro.getAutores()) {
            Optional<Autor> autorExistente = autorRepository.findByNombre(autor.getNombre());
            if (autorExistente.isPresent()) {
                autores.add(autorExistente.get());
            } else {
                autorRepository.save(autor);
                autores.add(autor);
            }
        }
        libro.setAutores(autores);
        for (Autor autor : autores) {
            if (autor.getLibros() == null) {
                autor.setLibros(new ArrayList<>()); // Verificar y asignar si es null
            }
            autor.getLibros().add(libro);
        }
    }

    public void imprimirLibro(Libro libro) {
        List<Autor> autores = libro.getAutores();

        System.out.printf("\nTitulo: %s\nAutores:\n", libro.getTitulo());
        for (Autor autor : autores) {
            System.out.printf("\t%s (%d-%d)\n", autor.getNombre(), autor.getAñoNacimiento(), autor.getAñoDefuncion());
        }
        System.out.printf("Descargas: %d\nIdiomas: %s\n", libro.getNumeroDescargas(), libro.getIdiomas());
    }

    public void imprimirLibros(List<Libro> libros) {
        for (Libro libro : libros) {
            imprimirLibro(libro);
        }
    }
}
