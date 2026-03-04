package example.dagger.checks;

public interface HealthCheck {
    String getName();
    boolean isHealthy();
    String getStatus();
}
