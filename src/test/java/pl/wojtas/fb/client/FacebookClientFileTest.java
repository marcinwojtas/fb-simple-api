package pl.wojtas.fb.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import pl.wojtas.fb.domain.City;
import pl.wojtas.fb.domain.Coords;
import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.domain.Post;
import pl.wojtas.fb.exception.InternalException;
import pl.wojtas.fb.exception.NotFoundException;
import pl.wojtas.fb.service.resource.file.reader.IResourceFileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(DataProviderRunner.class)
public class FacebookClientFileTest {

    @Value("${facebook.resource_dir_path}")
    private String path;

    private ObjectMapper mapper = mock(ObjectMapper.class);
    private IResourceFileReader fileReader = mock(IResourceFileReader.class);

    @Test
    @UseDataProvider("dataProviderFbSet")
    public void shouldReturnFbSet(Set<Facebook> fb) throws Exception {

        Set<File> files = new HashSet<File>() {{
            for (int i = 0; i < fb.size(); i++) {
                add(mock(File.class));
            }
        }};

        when(fileReader.readFromPath(path, "json")).thenReturn(new HashSet<File>() {{
            addAll(files);
        }});

        Iterator<Facebook> iterator = fb.iterator();

        for (File f : files) {
            when(mapper.readValue(f, Facebook.class)).thenReturn(iterator.next());
        }

        FacebookClientFile facebookClientFile = new FacebookClientFile(mapper, fileReader);
        Set<Facebook> result = facebookClientFile.getAll();

        assertThat(result.size(), is(equalTo(fb.size())));
        assertThat(result, is(equalTo(fb)));
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("dataProviderFbSet")
    public void shouldReturnNotFoundException(Set<Facebook> fb) throws Exception {
        Set<File> files = new HashSet<File>() {{
            for (int i = 0; i < fb.size(); i++) {
                add(mock(File.class));
            }
        }};

        when(fileReader.readFromPath(path, "json")).thenReturn(new HashSet<File>() {{
            addAll(files);
        }});

        Iterator<Facebook> iterator = fb.iterator();

        for (File f : files) {
            when(mapper.readValue(f, Facebook.class)).thenReturn(iterator.next());
        }

        FacebookClientFile facebookClientFile = new FacebookClientFile(mapper, fileReader);
        facebookClientFile.getById("not-existed-id");
    }

    @Test
    public void shouldReturnFacebookById() throws Exception {

        Set<Facebook> fb = new HashSet<Facebook>() {{
            add(new Facebook(
                "1",
                2L,
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
            ));
            add(new Facebook(
                "3",
                3L,
                "Johns",
                "Lee",
                "exmaple",
                "gender1",
                new City("p1l", "krk1", "state1", new Coords(3.2, 3.3)),
                "work one",
                new HashSet<String>() {{
                    add("c");
                    add("d");
                }},
                "school-3",
                "location-3",
                "relations-3",
                new ArrayList<Post>() {{
                    add(new Post("4", "p4"));
                    add(new Post("5", "p5"));
                }}
            ));
        }};

        Facebook expected = new Facebook(
            "3",
            3L,
            "Johns",
            "Lee",
            "exmaple",
            "gender1",
            new City("p1l", "krk1", "state1", new Coords(3.2, 3.3)),
            "work one",
            new HashSet<String>() {{
                add("c");
                add("d");
            }},
            "school-3",
            "location-3",
            "relations-3",
            new ArrayList<Post>() {{
                add(new Post("4", "p4"));
                add(new Post("5", "p5"));
            }}
        );

        Set<File> files = new HashSet<File>() {{
            for (int i = 0; i < fb.size(); i++) {
                add(mock(File.class));
            }
        }};

        when(fileReader.readFromPath(path, "json")).thenReturn(new HashSet<File>() {{
            addAll(files);
        }});

        Iterator<Facebook> iterator = fb.iterator();

        for (File f : files) {
            when(mapper.readValue(f, Facebook.class)).thenReturn(iterator.next());
        }

        FacebookClientFile facebookClientFile = new FacebookClientFile(mapper, fileReader);
        Facebook result = facebookClientFile.getById(expected.getId());

        assertThat(result, is(equalTo(expected)));
    }

    @Test(expected = InternalException.class)
    public void shouldReturnInternalExceptionFromMapper() throws Exception {
        Set<File> files = new HashSet<File>() {{
                add(mock(File.class));
        }};

        when(fileReader.readFromPath(path, "json")).thenReturn(new HashSet<File>() {{
            addAll(files);
        }});


        for (File f : files) {
            doThrow(new IOException()).when(mapper).readValue(f, Facebook.class);
        }

        FacebookClientFile facebookClientFile = new FacebookClientFile(mapper, fileReader);
        facebookClientFile.getAll();
    }

    @Test(expected = InternalException.class)
    public void shouldReturnInternalExceptionFileReader() throws Exception {

        doThrow(new IOException()).when(fileReader).readFromPath(path, "json");

        FacebookClientFile facebookClientFile = new FacebookClientFile(mapper, fileReader);
        facebookClientFile.getAll();
    }

    @DataProvider
    public static Object[][] dataProviderFbSet() {
        return new Object[][]{
            {
                new HashSet<Facebook>() {{
                    add(new Facebook(
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
                    ));
                    add(new Facebook(
                        "2",
                        2L,
                        "Krzysztof",
                        "Krawczyk",
                        "occupatil",
                        "gender",
                        new City("pl", "wrw", "state", new Coords(2.2, 2.3)),
                        "wrka",
                        new HashSet<String>() {{
                            add("b");
                            add("c");
                        }},
                        "sc",
                        "loc",
                        "res",
                        new ArrayList<Post>() {{
                            add(new Post("2", "p2"));
                            add(new Post("3", "p3"));
                        }}
                    ));
                    add(new Facebook(
                        "3",
                        3L,
                        "John",
                        "Lee",
                        "exmaple",
                        "gender1",
                        new City("p1l", "krk1", "state1", new Coords(3.2, 3.3)),
                        "work one",
                        new HashSet<String>() {{
                            add("c");
                            add("d");
                        }},
                        "school-3",
                        "location-3",
                        "relations-3",
                        new ArrayList<Post>() {{
                            add(new Post("4", "p4"));
                            add(new Post("5", "p5"));
                        }}
                    ));
                }}
            },
            {
                new HashSet<Facebook>()
            }
        };
    }
}
