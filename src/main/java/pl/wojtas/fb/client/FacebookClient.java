package pl.wojtas.fb.client;

import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.exception.InternalException;
import pl.wojtas.fb.exception.NotFoundException;

import java.util.Set;

public interface FacebookClient {
    Set<Facebook> getAll() throws InternalException;
    Facebook getById(String id) throws NotFoundException, InternalException;
}
