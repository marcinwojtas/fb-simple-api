package pl.wojtas.fb.service.resource.file.reader;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface IResourceFileReader {
    Set<File> readFromPath(String path, String extension) throws IOException;
}
