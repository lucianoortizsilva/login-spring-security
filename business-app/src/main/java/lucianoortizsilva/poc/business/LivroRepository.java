package lucianoortizsilva.poc.business;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("LivroRepository")
public interface LivroRepository extends PagingAndSortingRepository<Livro, Long> {

	Optional<Livro> findByIsbn(String isbn);

	Page<Livro> findByDescricaoContaining(String descricao, Pageable pageable);

	Page<Livro> findAll(Pageable pageable);
	
}