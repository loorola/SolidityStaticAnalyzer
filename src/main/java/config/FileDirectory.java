package config;

import java.io.File;

public final class FileDirectory {
    private FileDirectory(){};
    public static File tmp_root = new File("tmp");
    public static File report_root = new File("report");
    public static File cfg_root = new File("report/cfg");
    public static File ast_root = new File("report/ast");
    public static File scanning_config_root = new File("config");
    public static File cve_report_root = new File("report/cve");

}
