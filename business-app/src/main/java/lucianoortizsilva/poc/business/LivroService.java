package lucianoortizsilva.poc.business;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@formatter:off
@Service
public class LivroService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("LivroRepository")
	private LivroRepository livroRepository;
	
	public Page<Livro> findAll(final String descricao, final int page, final int size) {
		if (isEmpty(descricao)) {
			return livroRepository.findAll(PageRequest.of(page, size));
		} else {
			return livroRepository.findByDescricaoContaining(descricao, PageRequest.of(page, size));
		}
	}

	@Transactional("businessTransactionManager")
	public Livro insert(final Livro entity) {
		final Optional<Livro> livro = this.livroRepository.findByIsbn(entity.getIsbn());
		if (livro.isPresent()) {
			throw new LivroComIsbnDuplicadoException();
		} else {
			return this.livroRepository.save(entity);
		}
	}

	
	@Transactional("businessTransactionManager")
	public void delete(final Long id) {
		final Optional<Livro> livro = this.livroRepository.findById(id);
		if (livro.isPresent()) {
			this.livroRepository.deleteById(id);
		} else {
			throw new LivroNaoEncontradoException();
		}
	}

	public Livro findById(final Long id) {
		Optional<Livro> livro = livroRepository.findById(id);
		if (livro.isPresent()) {
			return livro.get();
		} else {
			throw new LivroNaoEncontradoException();
		}
	}
	
	@Transactional("businessTransactionManager")
	public void update(final LivroDTO dto) {
		final Optional<Livro> livro = this.livroRepository.findById(dto.getId());
		if (livro.isPresent()) {
			this.validarIsbnDuplicadoAoAtualizarDadosLivro(dto);
			this.livroRepository.save(this.convertToEntity(dto));
		} else {
			throw new LivroNaoEncontradoException();
		}
	}

	private void validarIsbnDuplicadoAoAtualizarDadosLivro(final LivroDTO dto) {
		final Optional<Livro> livroCadastrado = this.livroRepository.findByIsbn(dto.getIsbn());
		if (livroCadastrado.isPresent()) {
			final Long idLivroCadastrado = livroCadastrado.get().getId();
			final Long idLivroParaAtualizar = dto.getId();
			if (!idLivroCadastrado.equals(idLivroParaAtualizar)) {
				throw new LivroComIsbnDuplicadoException();
			}
		}
	}

	public Livro convertToEntity(final LivroDTO dto) {
		return modelMapper.map(dto, Livro.class);
	}

}