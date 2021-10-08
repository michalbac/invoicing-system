package pl.futurecollars.invoicing.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class FileService {

    private String path;

    public FileService() {
    }

    public List<String> readLine() {
        try {
            return Files.readLines(new File(path), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void write(String line) {
        File file = new File(path);
        if (file.isFile()) {
            try {
                Files.append(line + "\n", file, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.write((line + "\n").getBytes(), new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void overwrite(List<String> lines) {
        File oldFile = new File(path);
        oldFile.delete();
        for (String s : lines) {
            write(s);
        }
    }

    public void deleteLine(String id) {
        List<String> updatedLines = readLine().stream().filter(o -> !o.contains(id)).collect(Collectors.toList());
        overwrite(updatedLines);

    }
}
