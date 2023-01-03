package lucianoortizsilva.poc.config;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lucianoortizsilva.poc.business.Livro;
import lucianoortizsilva.poc.business.LivroRepository;

@Component
public class LoadDatabaseDefault implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	@Qualifier("LivroRepository")
	private LivroRepository livroRepository;

	@Override
	@Transactional("businessTransactionManager")
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		createLivroIfNotFound("978-1-119-61762-4", "Oracle Certified Professional Java SE 11 Programmer II", "Scott Selikoff", LocalDate.of(2020, 01, 01));
		createLivroIfNotFound("978-85-7608-325-2", "EJB 3 em acao", "Debu Panda", LocalDate.of(2009, 01, 01));
		createLivroIfNotFound("978-85-7608-224-8", "Codigo Limpo", "Robert C. Martin", LocalDate.of(2005, 01, 01));
		createLivroIfNotFound("978-85-7522-621-6", "Microsservices Pronto Para Produção", "Susan J. Fowler", LocalDate.of(2022, 03, 14));
		createLivroIfNotFound("978-85-254-3218-6", "Sapiens", "Yuval Noah Harari", LocalDate.of(1999, 04, 9));
		createLivroIfNotFound("978-85-7608-303-0", "Certificação Sun Java 6", "Kathy Sierra", LocalDate.of(2010, 11, 12));
		createLivroIfNotFound("978-85-7608-294-1", "Use a Cabeça Servlets & JSP", "Bryan Basham", LocalDate.of(2000, 07, 14));
	}

	private void createLivroIfNotFound(final String isbn, final String descricao, final String autor, final LocalDate dtLancamento) {
		Optional<Livro> livro = livroRepository.findByIsbn(isbn);
		if (livro.isEmpty()) {
			livroRepository.save(new Livro(isbn, descricao, autor, dtLancamento));
		}
	}

}