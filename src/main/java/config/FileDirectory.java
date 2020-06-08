package config;

import java.io.File;

public final class FileDirectory {
    private FileDirectory(){};
    public static File tmp_root = new File("tmp");
    public static File report_root = new File("report");
}
