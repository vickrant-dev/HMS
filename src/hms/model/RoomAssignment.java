package hms.model;

import java.time.LocalDate;

public final class RoomAssignment {

    private final int assignmentId;
    private final Room room;
    private final Staff staff;
    private final LocalDate assignmentDate;
    private final String assignmentType;
    private final String status;
    private final String notes;

    public RoomAssignment(Room room, Staff staff, LocalDate assignmentDate,
                          String assignmentType, String status, String notes) {
        this.assignmentId = 0;
        this.room = room;
        this.staff = staff;
        this.assignmentDate = assignmentDate;
        this.assignmentType = assignmentType;
        this.status = status;
        this.notes = notes;
    }

    public RoomAssignment(int assignmentId, Room room, Staff staff,
                          LocalDate assignmentDate, String assignmentType,
                          String status, String notes) {
        this.assignmentId = assignmentId;
        this.room = room;
        this.staff = staff;
        this.assignmentDate = assignmentDate;
        this.assignmentType = assignmentType;
        this.status = status;
        this.notes = notes;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public Room getRoom() {
        return room;
    }

    public Staff getStaff() {
        return staff;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public int getRoomId() {
        return room.getRoomId();
    }

    public int getStaffId() {
        return staff.getStaffId();
    }
}
