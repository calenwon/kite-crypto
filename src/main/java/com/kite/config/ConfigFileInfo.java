package com.kite.config;

/**
 * Configuration file object
 *
 * @author Calendar
 */
public class ConfigFileInfo {

    private String password;

    private String hint;

    private String filePath;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
