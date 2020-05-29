package bookle.exceptions;

@SuppressWarnings("serial")
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String msg) {
        super(msg);
    }
}