package app.service.logger;

public class StandardOutputLogger implements Logger {

    @Override
    public void Log(String message) {
        System.out.println(message);
    }
}
