package pl.wojtas.fb.service.facebook;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtas.fb.client.FacebookClient;
import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.domain.Post;
import pl.wojtas.fb.exception.InternalException;
import pl.wojtas.fb.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacebookServiceFile implements FacebookService {

    private FacebookClient facebookClient;
    private Logger logger = LoggerFactory.getLogger(FacebookServiceFile.class);

    @Autowired
    public FacebookServiceFile(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    public Facebook findById(String id) throws NotFoundException {
        try {
            return this.facebookClient.getById(id);
        } catch (InternalException e) {
            throw new NotFoundException(e);
        }
    }

    public Map<String, Long> findMostCommonWords() {
        Map<String, Long> commonWords = new HashMap<>();
        Set<Facebook> facebooks = new HashSet<>();

        try {
            facebooks = facebookClient.getAll();
        } catch (InternalException e) {
            logger.error("Internal error.", e);
        }

        for (Facebook facebook : facebooks) {
            List<Post> posts = facebook.getPosts();
            for (Post post : posts) {
                String message = post.getMessage();
                String[] words = message.split("\\W+");
                for (String w : words) {
                    if (NumberUtils.isCreatable(w) || w.isEmpty()) {
                        continue;
                    }

                    String word = w.toLowerCase();

                    if (commonWords.containsKey(word)) {
                        commonWords.put(word, commonWords.get(word) + 1);
                    } else {
                        commonWords.put(word, 1L);
                    }
                }
            }
        }

        return commonWords.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));

    }

    public Set<String> findPostIdsByKeyword(String keyword) {
        Set<String> postIds = new HashSet<>();
        Set<Facebook> facebooks = new HashSet<>();
        try {
            facebooks = facebookClient.getAll();
        } catch (InternalException e) {
            logger.error("Internal error", e);
        }

        for (Facebook facebook : facebooks) {
            List<Post> posts = facebook.getPosts();

            for (Post post : posts) {
                String message = post.getMessage();
                String[] words = message.split("\\W+");
                for (String word : words) {
                    if (word.equalsIgnoreCase(keyword)) {
                        postIds.add(post.getId());
                    }
                }
            }
        }

        return postIds;
    }

    public Set<Facebook> findAll() {
        Comparator<Facebook> comparator = Comparator.comparing(Facebook::getFirstname).thenComparing(Facebook::getLastname);
        Set<Facebook> facebooks = new TreeSet<>(comparator);

        try {
            facebooks.addAll(facebookClient.getAll());
        } catch (InternalException e) {
            logger.error("Internal error.", e);
        }

        return facebooks;
    }
}
