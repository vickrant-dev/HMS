package hms.model;

import java.time.LocalDateTime;

/**
 * Represents a hotel service.
 */
public final class Service {

    private final int serviceId;
    private final String serviceName;
    private final String serviceType;
    private final double price;
    private final String description;
    private final boolean isAvailable;
    private final LocalDateTime createdAt;

    /**
     * Creates a new service without an ID (for new records).
     *
     * @param serviceName The name of the service
     * @param serviceType The type of service (e.g., "Food", "Laundry", "Spa")
     * @param price       The price of the service
     * @param description A description of the service
     * @param isAvailable Whether the service is currently available
     */
    public Service(String serviceName, String serviceType, double price,
                   String description, boolean isAvailable) {
        this.serviceId = 0;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.price = price;
        this.description = description;
        this.isAvailable = isAvailable;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a service with all fields (for database reconstruction).
     *
     * @param serviceId   The service's unique ID
     * @param serviceName The name of the service
     * @param serviceType The type of service
     * @param price       The price of the service
     * @param description A description of the service
     * @param isAvailable Whether the service is currently available
     * @param createdAt   The timestamp when the record was created
     */
    public Service(int serviceId, String serviceName, String serviceType,
                   double price, String description, boolean isAvailable,
                   LocalDateTime createdAt) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.price = price;
        this.description = description;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    /** Returns the service's unique ID. */
    public int getServiceId() {
        return serviceId;
    }

    /** Returns the name of the service. */
    public String getServiceName() {
        return serviceName;
    }

    /** Returns the type of service. */
    public String getServiceType() {
        return serviceType;
    }

    /** Returns the price of the service. */
    public double getPrice() {
        return price;
    }

    /** Returns a description of the service. */
    public String getDescription() {
        return description;
    }

    /** Returns whether the service is currently available. */
    public boolean isAvailable() {
        return isAvailable;
    }

    /** Returns the timestamp when the record was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
