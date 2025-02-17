package cz.cvut.fit.tjv.easyvps_client.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}