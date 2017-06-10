package pl.wojtas.fb.exception;

public class NotFoundException extends Exception{
    public NotFoundException() {
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
