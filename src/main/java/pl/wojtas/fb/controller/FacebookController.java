package pl.wojtas.fb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.exception.NotFoundException;
import pl.wojtas.fb.service.facebook.FacebookService;

import java.util.Map;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/facebook")
public class FacebookController {

    private FacebookService fb;

    @Autowired
    public FacebookController(FacebookService fb) {
        this.fb = fb;
    }

    @RequestMapping(path = "/{id}", method = GET)
    public Facebook getAction(@PathVariable("id") String id) throws NotFoundException {
        return fb.findById(id);
    }

    @RequestMapping(path = "", method = GET)
    public Set<Facebook> listAction() {
        return fb.findAll();
    }

    @RequestMapping(path = "/common-words/", method = GET)
    public Map<String, Long> getCommonWordsAction() {
        return fb.findMostCommonWords();
    }

    @RequestMapping(path = "/posts/", method = GET)
    public Set<String> getPostByWords(@RequestParam(value = "word") String word) {
        return fb.findPostIdsByKeyword(word);
    }
}