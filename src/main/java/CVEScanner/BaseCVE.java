package CVEScanner;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCVE {
    public String name;
    public String description;
    public String xpath;
    public boolean enabled;
    public int index;

    public abstract List<String> scan();
}
