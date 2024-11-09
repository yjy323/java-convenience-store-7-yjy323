package store.view;

import static store.ErrorMessages.FILE_OPEN_FAIL;
import static store.ErrorMessages.FILE_READ_FAIL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private FileReader openFile(String fileName) {
        try {
            return new FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(String.format(FILE_OPEN_FAIL.getMessage(), fileName));
        }
    }

    private List<String> readContents(BufferedReader br) throws IOException {
        List<String> contents = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            contents.add(line);
            line = br.readLine();
        }
        return contents;
    }

    public List<String> read(String fileName) {
        BufferedReader br = new BufferedReader(openFile(fileName));
        try {
            return readContents(br);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(FILE_READ_FAIL.getMessage(), fileName));
        }
    }
}
