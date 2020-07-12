package CVEScanner;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BaseCVE {
    public String name;
    public String description;
    List<Pair<String,String>> patternList = new ArrayList<>();
    public String xpath;
    public String pattern;
}
