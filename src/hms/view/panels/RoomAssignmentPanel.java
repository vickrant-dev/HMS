/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hms.view.panels;

import hms.config.Constants;
import hms.controller.StaffController;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.RoomAssignment;
import hms.model.Staff;
import java.awt.Frame;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import hms.view.dialogs.NewRoomAssignmentDialog;
import hms.util.IconUtil;

/**
 *
 * @author vickrant-dev
 */
public class RoomAssignmentPanel extends javax.swing.JPanel {

    private final StaffController staffController = new StaffController();
    private List<RoomAssignment> filteredAssignments;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RoomAssignmentPanel.class.getName());

    /**
     * Creates new form RoomAssignmentPanel
     */
    public RoomAssignmentPanel() {
        initComponents();
        statusCmb.removeAllItems();
        statusCmb.addItem("All");
        statusCmb.addItem(Constants.ASSIGN_PENDING);
        statusCmb.addItem(Constants.ASSIGN_IN_PROGRESS);
        statusCmb.addItem(Constants.ASSIGN_COMPLETED);
        staffCmb.removeAllItems();
        staffCmb.addItem("All");
        try {
            for (Staff s : staffController.getAllStaff()) {
                staffCmb.addItem(s.getFirstName() + " " + s.getLastName());
            }
        } catch (DatabaseException e) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to load staff for combo", e);
        }
        setupTable();
        setupPaginationListeners();
        setupIcons();
        loadAssignments();
    }

    private void setupPaginationListeners() {
        roomAssignmentPaginationLeft.addActionListener(e -> {
            if (currentPage > 0) { currentPage--; applyPagination(); }
        });
        roomAssignmentPaginationRight.addActionListener(e -> {
            int totalPages = Math.max(1, (int) Math.ceil((double) filteredAssignments.size() / PAGE_SIZE));
            if (currentPage < totalPages - 1) { currentPage++; applyPagination(); }
        });
    }

    private void loadAssignments() {
        try {
            filteredAssignments = staffController.getAllAssignments();
            currentPage = 0;
            applyPagination();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "Failed to load assignments: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyPagination() {
        int total = filteredAssignments.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        if (currentPage < 0) currentPage = 0;

        int from = currentPage * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, total);
        List<RoomAssignment> page = from < total ? filteredAssignments.subList(from, to) : List.of();

        String[][] data = new String[page.size()][7];
        for (int i = 0; i < page.size(); i++) {
            RoomAssignment a = page.get(i);
            data[i][0] = String.valueOf(a.getAssignmentId());
            data[i][1] = a.getRoom().getRoomNumber();
            data[i][2] = a.getStaff().getFirstName() + " " + a.getStaff().getLastName();
            data[i][3] = a.getAssignmentType();
            data[i][4] = a.getAssignmentDate().toString();
            data[i][5] = a.getStatus();
            data[i][6] = a.getNotes() != null ? a.getNotes() : "";
        }

        roomAssignmentsTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{
            "#", "ROOM NO", "STAFF NAME", "ASSIGNMENT TYPE", "DATE", "STATUS", "NOTES"
        }) {
            boolean[] canEdit = {false, false, false, false, false, false, false};
            @Override public boolean isCellEditable(int row, int col) { return canEdit[col]; }
        });

        pageNumber3.setText("Page " + (currentPage + 1) + " of " + totalPages);
        totalRecords2.setText("Records: " + total);
    }

    private RoomAssignment getSelectedAssignment() {
        int viewRow = roomAssignmentsTable.getSelectedRow();
        if (viewRow == -1 || filteredAssignments == null) return null;
        return filteredAssignments.get(roomAssignmentsTable.convertRowIndexToModel(viewRow));
    }

    private void setupTable() {
        roomAssignmentsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = roomAssignmentsTable.getSelectedRow() != -1;
            editAssignBtn.setEnabled(hasSelection);
            markInProgressBtn.setEnabled(hasSelection);
            markCompletedBtn.setEnabled(hasSelection);
            deleteAssignBtn.setEnabled(hasSelection);
        });
        editAssignBtn.setEnabled(false);
        markInProgressBtn.setEnabled(false);
        markCompletedBtn.setEnabled(false);
        deleteAssignBtn.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        heading = new javax.swing.JLabel();
        description = new javax.swing.JLabel();
        addAssignmentBtn = new javax.swing.JButton();
        guest_seperator_1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        room_seperator_2 = new javax.swing.JSeparator();
        statusCmb = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        searchBox = new javax.swing.JTextField();
        clearBtn = new javax.swing.JButton();
        searchBtn = new javax.swing.JButton();
        dateChooser = new com.toedter.calendar.JDateChooser();
        applyFiltersBtn = new javax.swing.JButton();
        staffCmb = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        roomAssignmentsTable = new javax.swing.JTable();
        bottomBar1 = new javax.swing.JPanel();
        editAssignBtn = new javax.swing.JButton();
        markInProgressBtn = new javax.swing.JButton();
        markCompletedBtn = new javax.swing.JButton();
        roomAssignmentPaginationLeft = new javax.swing.JButton();
        roomAssignmentPaginationRight = new javax.swing.JButton();
        pageNumber3 = new javax.swing.JLabel();
        totalRecords2 = new javax.swing.JLabel();
        deleteAssignBtn = new javax.swing.JButton();

        heading.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        heading.setText("Room Assignments");

        description.setText("Manage daily staff duties and property maintenance.");

        addAssignmentBtn.setText("Add Assignment");
        addAssignmentBtn.addActionListener(this::addAssignmentBtnActionPerformed);

        jLabel1.setText("Search by room no.");

        room_seperator_2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        statusCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));

        jLabel2.setText("Status");

        jLabel3.setText("Date");

        searchBox.setToolTipText("Search by room no.");

        clearBtn.setText("Clear");
        clearBtn.addActionListener(this::clearBtnActionPerformed);

        searchBtn.setText("Search");
        searchBtn.addActionListener(this::searchBtnActionPerformed);

        applyFiltersBtn.setText("Apply Filters");
        applyFiltersBtn.addActionListener(this::applyFiltersBtnActionPerformed);

        staffCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));

        jLabel4.setText("Staff");

        roomAssignmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "ROOM NO", "STAFF NAME", "ASSIGNMENT TYPE", "DATE", "STATUS", "NOTES"
            }
        ));
        jScrollPane1.setViewportView(roomAssignmentsTable);

        bottomBar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(76, 76, 76), 1, true));

        editAssignBtn.setText("Edit");
        editAssignBtn.addActionListener(this::editAssignBtnActionPerformed);

        markInProgressBtn.setText("Mark In Progress");
        markInProgressBtn.addActionListener(this::markInProgressBtnActionPerformed);

        markCompletedBtn.setText("Mark Completed");
        markCompletedBtn.addActionListener(this::markCompletedBtnActionPerformed);

        roomAssignmentPaginationLeft.setText("<");

        roomAssignmentPaginationRight.setText(">");

        pageNumber3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pageNumber3.setText("Page 1 of 5");

        totalRecords2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalRecords2.setText("Records: 20");

        deleteAssignBtn.setText("Delete");
        deleteAssignBtn.setToolTipText("");
        deleteAssignBtn.addActionListener(this::deleteAssignBtnActionPerformed);

        javax.swing.GroupLayout bottomBar1Layout = new javax.swing.GroupLayout(bottomBar1);
        bottomBar1.setLayout(bottomBar1Layout);
        bottomBar1Layout.setHorizontalGroup(
            bottomBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBar1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editAssignBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(markInProgressBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(markCompletedBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteAssignBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                .addComponent(totalRecords2)
                .addGap(18, 18, 18)
                .addComponent(roomAssignmentPaginationLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roomAssignmentPaginationRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pageNumber3)
                .addContainerGap())
        );
        bottomBar1Layout.setVerticalGroup(
            bottomBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBar1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editAssignBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(markInProgressBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(markCompletedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pageNumber3)
                    .addComponent(roomAssignmentPaginationLeft)
                    .addComponent(roomAssignmentPaginationRight)
                    .addComponent(totalRecords2)
                    .addComponent(deleteAssignBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(guest_seperator_1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(heading)
                            .addComponent(description))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addAssignmentBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchBtn)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(room_seperator_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(statusCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(staffCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                                .addComponent(clearBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(applyFiltersBtn))))
                    .addComponent(bottomBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addAssignmentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(heading)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(description)))
                .addGap(18, 18, 18)
                .addComponent(guest_seperator_1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(room_seperator_2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(statusCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(applyFiltersBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staffCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bottomBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addAssignmentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAssignmentBtnActionPerformed
        NewRoomAssignmentDialog d = new NewRoomAssignmentDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        d.setVisible(true);
        loadAssignments();
    }//GEN-LAST:event_addAssignmentBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        String keyword = searchBox.getText().trim().toLowerCase();
        try {
            filteredAssignments = keyword.isEmpty()
                ? staffController.getAllAssignments()
                : staffController.getAllAssignments().stream()
                    .filter(a -> a.getRoom().getRoomNumber().contains(keyword)
                        || a.getStaff().getFirstName().toLowerCase().contains(keyword)
                        || a.getStaff().getLastName().toLowerCase().contains(keyword)
                        || a.getAssignmentType().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
            currentPage = 0;
            applyPagination();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_searchBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        searchBox.setText("");
        statusCmb.setSelectedIndex(0);
        staffCmb.setSelectedIndex(0);
        dateChooser.setDate(null);
        loadAssignments();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void applyFiltersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyFiltersBtnActionPerformed
        try {
            List<RoomAssignment> all = staffController.getAllAssignments();
            String status = (String) statusCmb.getSelectedItem();
            String staffName = (String) staffCmb.getSelectedItem();
            java.util.Date date = dateChooser.getDate();

            filteredAssignments = all.stream()
                .filter(a -> status == null || status.equals("All") || a.getStatus().equalsIgnoreCase(status.replace(" ", "_")))
                .filter(a -> staffName == null || staffName.equals("All")
                    || (a.getStaff().getFirstName() + " " + a.getStaff().getLastName()).equalsIgnoreCase(staffName))
                .filter(a -> date == null || a.getAssignmentDate().equals(
                    date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()))
                .collect(Collectors.toList());

            currentPage = 0;
            applyPagination();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "Filter failed: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_applyFiltersBtnActionPerformed

    private void editAssignBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAssignBtnActionPerformed
        RoomAssignment selected = getSelectedAssignment();
        if (selected == null) return;
        NewRoomAssignmentDialog d = new NewRoomAssignmentDialog((Frame) SwingUtilities.getWindowAncestor(this), true, selected);
        d.setTitle("Edit Assignment #" + selected.getAssignmentId());
        d.setVisible(true);
        loadAssignments();
    }//GEN-LAST:event_editAssignBtnActionPerformed

    private void markInProgressBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markInProgressBtnActionPerformed
        RoomAssignment selected = getSelectedAssignment();
        if (selected == null) return;
        try {
            staffController.updateAssignmentStatus(selected.getAssignmentId(), "IN_PROGRESS");
            loadAssignments();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "Failed to update status: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_markInProgressBtnActionPerformed

    private void markCompletedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markCompletedBtnActionPerformed
        RoomAssignment selected = getSelectedAssignment();
        if (selected == null) return;
        try {
            staffController.updateAssignmentStatus(selected.getAssignmentId(), "COMPLETED");
            loadAssignments();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "Failed to update status: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_markCompletedBtnActionPerformed

    private void deleteAssignBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAssignBtnActionPerformed
        RoomAssignment selected = getSelectedAssignment();
        if (selected == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete assignment #" + selected.getAssignmentId() + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                staffController.deleteAssignment(selected.getAssignmentId());
                loadAssignments();
            } catch (DatabaseException e) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteAssignBtnActionPerformed


    private void setupIcons() {
        addAssignmentBtn.setIcon(IconUtil.getAddIcon());
        editAssignBtn.setIcon(IconUtil.getEditIcon());
        deleteAssignBtn.setIcon(IconUtil.getDeleteIcon());
        markCompletedBtn.setIcon(IconUtil.getCheckIcon());
        markInProgressBtn.setIcon(IconUtil.getWrenchIcon());
        searchBtn.setIcon(IconUtil.getSearchIcon());
        clearBtn.setIcon(IconUtil.getRefreshIcon());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAssignmentBtn;
    private javax.swing.JButton applyFiltersBtn;
    private javax.swing.JPanel bottomBar1;
    private javax.swing.JButton clearBtn;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JButton deleteAssignBtn;
    private javax.swing.JLabel description;
    private javax.swing.JButton editAssignBtn;
    private javax.swing.JSeparator guest_seperator_1;
    private javax.swing.JLabel heading;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton markCompletedBtn;
    private javax.swing.JButton markInProgressBtn;
    private javax.swing.JLabel pageNumber3;
    private javax.swing.JButton roomAssignmentPaginationLeft;
    private javax.swing.JButton roomAssignmentPaginationRight;
    private javax.swing.JTable roomAssignmentsTable;
    private javax.swing.JSeparator room_seperator_2;
    private javax.swing.JTextField searchBox;
    private javax.swing.JButton searchBtn;
    private javax.swing.JComboBox<String> staffCmb;
    private javax.swing.JComboBox<String> statusCmb;
    private javax.swing.JLabel totalRecords2;
    // End of variables declaration//GEN-END:variables
}
