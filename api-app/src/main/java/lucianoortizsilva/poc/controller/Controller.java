package lucianoortizsilva.poc.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//@formatter:off
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/livros")
public class Controller {

	
	
	@PostMapping
	@PreAuthorize("permitAll")
	public ResponseEntity<Void> insert(@RequestBody @Valid final LivroDTO dto) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_FUNCIONARIO')")
	public ResponseEntity<Void> update(@RequestBody @Valid @P("dto") final LivroDTO dto, @PathVariable(value = "id", required = true) final Long id) {
		dto.setId(id);
		return ResponseEntity.noContent().build();
	}

	
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) final Long id) {
		return ResponseEntity.noContent().build();
	}

	
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_FUNCIONARIO') or hasAuthority('ROLE_SUPORTE')")
	public ResponseEntity<LivroDTO> findById(@PathVariable(value = "id", required = true) final Long id) {
		LivroDTO dto = new LivroDTO(id, null, null, null, null);
		return ResponseEntity.ok().body(dto);
	}

}