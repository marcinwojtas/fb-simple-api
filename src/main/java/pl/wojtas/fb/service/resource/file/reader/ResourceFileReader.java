package pl.wojtas.fb.service.resource.file.reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class ResourceFileReader implements IResourceFileReader {

    public Set<File> readFromPath(String path, String extension) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = resolver.getResources("classpath*:/" + path + "/*." + extension);

        Set<File> files = new HashSet<>();

        for (Resource resource : resources) {
            files.add(resource.getFile());
        }

        return files;
    }
}
