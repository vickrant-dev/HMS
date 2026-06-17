package hms.model;

import java.time.LocalDate;

/**
 * Represents an assignment of staff to a room.
 */
public final class RoomAssignment {

    private final int assignmentId;
    private final Room room;
    private final Staff staff;
    private final LocalDate assignmentDate;
    private final String assignmentType;
    private final String status;
    private final String notes;

    /**
     * Creates a new room assignment without an ID (for new records).
     *
     * @param room           The assigned room
     * @param staff          The assigned staff member
     * @param assignmentDate The date of the assignment
     * @param assignmentType The type of assignment (e.g., "Cleaning", "Maintenance")
     * @param status         The assignment status
     * @param notes          Additional notes
     */
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

    /**
     * Creates a room assignment with all fields (for database reconstruction).
     *
     * @param assignmentId   The assignment's unique ID
     * @param room           The assigned room
     * @param staff          The assigned staff member
     * @param assignmentDate The date of the assignment
     * @param assignmentType The type of assignment
     * @param status         The assignment status
     * @param notes          Additional notes
     */
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

    /** Returns the assignment's unique ID. */
    public int getAssignmentId() {
        return assignmentId;
    }

    /** Returns the assigned room. */
    public Room getRoom() {
        return room;
    }

    /** Returns the assigned staff member. */
    public Staff getStaff() {
        return staff;
    }

    /** Returns the date of the assignment. */
    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    /** Returns the type of assignment. */
    public String getAssignmentType() {
        return assignmentType;
    }

    /** Returns the assignment status. */
    public String getStatus() {
        return status;
    }

    /** Returns additional notes. */
    public String getNotes() {
        return notes;
    }

    /** Returns the assigned room's ID. */
    public int getRoomId() {
        return room.getRoomId();
    }

    /** Returns the assigned staff member's ID. */
    public int getStaffId() {
        return staff.getStaffId();
    }
}
