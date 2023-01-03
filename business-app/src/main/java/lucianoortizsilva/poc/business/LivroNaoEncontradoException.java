package lucianoortizsilva.poc.business;

import lucianoortizsilva.poc.exception.NaoEncontradoException;

public class LivroNaoEncontradoException extends NaoEncontradoException {

	private static final long serialVersionUID = -6858656831206266702L;

	public LivroNaoEncontradoException() {
		super("Livro não encontrado");
	}

}