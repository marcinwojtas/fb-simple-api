package pl.wojtas.fb.service.facebook;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.wojtas.fb.client.FacebookClient;
import pl.wojtas.fb.domain.City;
import pl.wojtas.fb.domain.Coords;
import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.domain.Post;
import pl.wojtas.fb.exception.InternalException;
import pl.wojtas.fb.exception.NotFoundException;

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class FacebookServiceFileTest {

    private FacebookClient facebookClient = mock(FacebookClient.class);

    @UseDataProvider("dataProviderFb")
    @Test
    public void shouldGetById(String id, Facebook from, Facebook out) throws Exception {
        when(facebookClient.getById(id)).thenReturn(from);
        FacebookService facebookService = new FacebookServiceFile(facebookClient);
        Facebook result = facebookService.findById(id);
        assertThat(result, is(equalTo(out)));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenClientThrowNotFound() throws Exception {
        when(facebookClient.getById(any())).thenThrow(new NotFoundException());
        FacebookService facebookService = new FacebookServiceFile(facebookClient);
        facebookService.findById("1");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenClientThrowInternal() throws Exception {
        when(facebookClient.getById(any())).thenThrow(new InternalException());
        FacebookService facebookService = new FacebookServiceFile(facebookClient);
        facebookService.findById("1");
    }

    @UseDataProvider("dataProviderPostsCommonWords")
    @Test
    public void shouldFindMostCommonWords(List<List<Post>> posts, Map<String, Long> commonWords) throws Exception {
        Set<Facebook> facebooks = new HashSet<>();

        for (List<Post> postList : posts) {
            Facebook fb = mock(Facebook.class);
            when(fb.getPosts()).thenReturn(postList);

            facebooks.add(fb);
        }

        when(facebookClient.getAll()).thenReturn(facebooks);

        FacebookService facebookService = new FacebookServiceFile(facebookClient);
        Map<String, Long> resultCommonWords = facebookService.findMostCommonWords();

        assertThat(resultCommonWords, is(equalTo(commonWords)));
    }

    @UseDataProvider("dataProviderPostsKeywords")
    @Test
    public void shouldFindPostsByKeywords(String keyword, List<List<Post>> posts, Set<String> ids) throws Exception {
        Set<Facebook> facebooks = new HashSet<>();

        for (List<Post> postList : posts) {
            Facebook fb = mock(Facebook.class);
            when(fb.getPosts()).thenReturn(postList);

            facebooks.add(fb);
        }

        when(facebookClient.getAll()).thenReturn(facebooks);

        FacebookService facebookService = new FacebookServiceFile(facebookClient);
        Set<String> resultIds = facebookService.findPostIdsByKeyword(keyword);

        assertThat(resultIds, is(equalTo(ids)));
    }

    @UseDataProvider("dataProviderFbSort")
    @Test
    public void shouldFindAllSortedByFirstNameAndLastName(Set<Facebook> fromClient, List expectedOrder) throws Exception {
        when(facebookClient.getAll()).thenReturn(fromClient);

        FacebookService facebookService = new FacebookServiceFile(facebookClient);
        Set<Facebook> result = facebookService.findAll();

        List<String> resultOrder = new ArrayList<>();

        for (Facebook fb : result) {
            resultOrder.add(fb.getId());
        }

        assertThat(resultOrder, is(equalTo(expectedOrder)));
    }

    @DataProvider
    public static Object[][] dataProviderFb() {
        return new Object[][]{
            {"1",
                new Facebook(
                    "1",
                    1L,
                    "Marcin",
                    "Wojtas",
                    "occupation-exmapl",
                    "gender",
                    new City("pl", "krk", "state", new Coords(1.2, 1.3)),
                    "wrk",
                    new HashSet<String>() {{
                        add("a");
                        add("b");
                    }},
                    "school",
                    "location",
                    "relations",
                    new ArrayList<Post>() {{
                        add(new Post("1", "p1"));
                        add(new Post("2", "p2"));
                    }}
                ),
                new Facebook(
                    "1",
                    1L,
                    "Marcin",
                    "Wojtas",
                    "occupation-exmapl",
                    "gender",
                    new City("pl", "krk", "state", new Coords(1.2, 1.3)),
                    "wrk",
                    new HashSet<String>() {{
                        add("a");
                        add("b");
                    }},
                    "school",
                    "location",
                    "relations",
                    new ArrayList<Post>() {{
                        add(new Post("1", "p1"));
                        add(new Post("2", "p2"));
                    }}
                )
            }
        };
    }

    @DataProvider
    public static Object[][] dataProviderPostsCommonWords() {
        return new Object[][]{
            {
                new ArrayList<List<Post>>() {{
                    add(new ArrayList<Post>() {{
                        add(new Post("1", "three three"));
                        add(new Post("2", "one three"));
                    }});
                }},
                new LinkedHashMap<String, Long>() {{
                    put("three", 3L);
                    put("one", 1L);
                }}
            },
            {
                new ArrayList<List<Post>>() {{
                    add(new ArrayList<Post>() {{
                        add(new Post("1", "One, one, ONE, one"));
                        add(new Post("2", "ONE one, two, two two and ONE"));
                    }});
                }},
                new LinkedHashMap<String, Long>() {{
                    put("one", 7L);
                    put("two", 3L);
                    put("and", 1L);
                }}
            },
            {
                new ArrayList<List<Post>>() {{
                    add(new ArrayList<Post>() {{
                        add(new Post("1", " ,.;/.  "));
                        add(new Post("2", " ,  2 355. ; / .  "));
                        add(new Post("3", "\'\'"));
                    }});
                }},
                new LinkedHashMap<String, Long>() {{
                }}
            }
        };
    }

    @DataProvider
    public static Object[][] dataProviderPostsKeywords() {
        return new Object[][]{
            {
                "word",
                new ArrayList<List<Post>>() {{
                    add(new ArrayList<Post>() {{
                        add(new Post("1", "word sds three"));
                        add(new Post("2", "one word three"));
                        add(new Post("3", "one woord three"));
                    }});
                }},
                new HashSet<String>() {{
                    add("2");
                    add("1");
                }}
            },
            {
                "word",
                new ArrayList<List<Post>>() {{
                    add(new ArrayList<Post>() {{
                        add(new Post("1", ",word, word, word, sds three"));
                        add(new Post("2", "one three"));
                        add(new Post("3", "one ,word, three"));
                    }});
                }},
                new HashSet<String>() {{
                    add("3");
                    add("1");
                }}
            },
            {
                "no-words",
                new ArrayList<List<Post>>() {{
                    add(new ArrayList<Post>() {{
                        add(new Post("1", ",word, word, word, sds three"));
                        add(new Post("2", "one three"));
                        add(new Post("3", "one ,word, three"));
                    }});
                }},
                new HashSet<String>() {{
                }}
            },
        };
    }

    @DataProvider
    public static Object[][] dataProviderFbSort() {
        return new Object[][]{
            {
                new HashSet<Facebook>() {{
                    add(new Facebook(
                        "1",
                        1L,
                        "Krzysztof",
                        "Krawczyk",
                        "occupation-exmapl",
                        "gender",
                        new City("pl", "krk", "state", new Coords(1.2, 1.3)),
                        "wrk",
                        new HashSet<String>() {{
                            add("a");
                            add("b");
                        }},
                        "school",
                        "location",
                        "relations",
                        new ArrayList<Post>() {{
                            add(new Post("1", "p1"));
                            add(new Post("2", "p2"));
                        }}
                    ));
                    add(new Facebook(
                        "2",
                        1L,
                        "Adam",
                        "Krawczyk",
                        "occupation-exmapl",
                        "gender",
                        new City("pl", "krk", "state", new Coords(1.2, 1.3)),
                        "wrk",
                        new HashSet<String>() {{
                            add("a");
                            add("b");
                        }},
                        "school",
                        "location",
                        "relations",
                        new ArrayList<Post>() {{
                            add(new Post("1", "p1"));
                            add(new Post("2", "p2"));
                        }}
                    ));
                    add(new Facebook(
                        "3",
                        1L,
                        "Krzysztof",
                        "Adamczyk",
                        "occupation-exmapl",
                        "gender",
                        new City("pl", "krk", "state", new Coords(1.2, 1.3)),
                        "wrk",
                        new HashSet<String>() {{
                            add("a");
                            add("b");
                        }},
                        "school",
                        "location",
                        "relations",
                        new ArrayList<Post>() {{
                            add(new Post("1", "p1"));
                            add(new Post("2", "p2"));
                        }}
                    ));
                }},
                new ArrayList<String>() {{
                    add("2");
                    add("3");
                    add("1");
                }}
            },
            {
                new HashSet<Facebook>(),
                new ArrayList<String>()
            }
        };
    }
}
