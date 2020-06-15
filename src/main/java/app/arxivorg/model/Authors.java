package app.arxivorg.model;

import java.util.ArrayList;
import java.util.List;

public class Authors {
    private ArrayList<String> data;

    public Authors(List<String> input) {
        this.data = new ArrayList<>(input);
    }

    public String toString() {
        return String.join(", ", data);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public boolean contains(String author){
        return this.data.contains(author);
    }
}
