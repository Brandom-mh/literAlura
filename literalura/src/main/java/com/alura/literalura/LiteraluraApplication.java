package com.alura.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alura.literalura.principal.Principal;
import com.alura.literalura.repositorio.AutorRepository;
import com.alura.literalura.repositorio.LibroRepository;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner{

	@Autowired
	private LibroRepository repository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository,autorRepository);
		principal.menu();
	}

}