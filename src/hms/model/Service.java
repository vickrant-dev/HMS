package hms.model;

import java.time.LocalDateTime;

public final class Service {

    private final int serviceId;
    private final String serviceName;
    private final String serviceType;
    private final double price;
    private final String description;
    private final boolean isAvailable;
    private final LocalDateTime createdAt;

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

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
