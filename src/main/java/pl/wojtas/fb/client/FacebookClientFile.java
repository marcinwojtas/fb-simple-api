package pl.wojtas.fb.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.wojtas.fb.domain.Facebook;
import pl.wojtas.fb.exception.InternalException;
import pl.wojtas.fb.exception.NotFoundException;
import pl.wojtas.fb.service.resource.file.reader.IResourceFileReader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class FacebookClientFile implements FacebookClient {

    @Value("${facebook.resource_dir_path}")
    private String resourceDirPath;

    private ObjectMapper mapper;
    private IResourceFileReader resourceFileReader;

    @Autowired
    public FacebookClientFile(ObjectMapper mapper, IResourceFileReader resourceFileReader) {
        this.mapper = mapper;
        this.resourceFileReader = resourceFileReader;
    }


    public Set<Facebook> getAll() throws InternalException {
        Set<File> files = new HashSet<>();

        try {
            files.addAll(resourceFileReader.readFromPath(resourceDirPath, "json"));
        } catch (IOException e) {
            throw new InternalException(e);
        }

        Set<Facebook> facebooks = new HashSet<>();

        for (File file : files) {
            try {
                facebooks.add(mapper.readValue(file, Facebook.class));
            } catch (IOException e) {
                throw new InternalException(e);
            }
        }

        return facebooks;
    }

    public Facebook getById(String id) throws NotFoundException, InternalException {
        Set<Facebook> facebooks = getAll();

        for (Facebook facebook : facebooks) {
            if (facebook.getId().equals(id)) {
                return facebook;
            }
        }

        throw new NotFoundException();
    }
}