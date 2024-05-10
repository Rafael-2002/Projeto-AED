package pt.ulusofona.aed.deisiworldmeter;

public class Queries {

    boolean success;

    String error;
    String result;

    public Queries(boolean success, String error, String result) {
        this.success = success;
        this.error = error;
        this.result = result;
    }
}
