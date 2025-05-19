package br.jus.tjro.gabinete.model.gab;

public class Version {
    private final String number;
    private final String timestamp;
    private final String commitSha;

    private Version() {
        this.number = "";
        this.timestamp = "";
        this.commitSha = "";
    }

    public Version(String number, String timestamp, String commitSha) {
        this.number = number;
        this.timestamp = timestamp;
        this.commitSha = commitSha;
    }

    public String getNumber() {
        return number;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCommitSha() {
        return commitSha;
    }
}
