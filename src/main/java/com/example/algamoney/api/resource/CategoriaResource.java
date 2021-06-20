package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	/*LISTAR*/
	//@CrossOrigin(maxAge = 10, origins = {"http://localhost:8080"}) //defini o que pode ser liberado na cross
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public  List<Categoria> listar(){
		
		//return categoriaRepository.findAll();
		return (List<Categoria>) categoriaRepository.findAll();
	}
	
	/*----------------------SALVAR NOVOS REGISTROS----------------------------*/
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED) //STATUS 201 CREATED
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid  @RequestBody Categoria categoria , HttpServletResponse response) {
		
		Categoria categoriaSalvar = categoriaRepository.save(categoria);
		/*URI uri = ServletUriComponentsBuilder
		.fromCurrentRequestUri()
		.path("/{codigo}")
		.buildAndExpand(categoriaSalvar.getCodigo())
		.toUri();*/
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalvar.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalvar);
		
	}
	/*BUSCANDO PELO CODIGO*/
	/*
	@GetMapping("/{codigo}")
	public Categoria buscarPeloCodigo(@PathVariable Long codigo) {
	  return this.categoriaRepository.findById(codigo).orElse(null);
	}
	*/
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Optional<Categoria>> buscarPeloCodigo(@PathVariable Long codigo) {
		 Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		 return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	

}
