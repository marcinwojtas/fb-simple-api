package pl.wojtas.fb.service.facebook;

import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.exception.NotFoundException;

import java.util.Map;
import java.util.Set;

public interface FacebookService {
    Facebook findById(String id) throws NotFoundException;
    Map<String, Long> findMostCommonWords();
    Set<String> findPostIdsByKeyword(String word);
    Set<Facebook> findAll();
}
