/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import dao.CivilstatusDao;
import dao.DaoException;
import dao.DesignationDao;
import dao.EmployeeDao;
import dao.EmployeestatusDao;
import dao.GenderDao;
import entity.Civilstatus;
import entity.Designation;
import entity.Employee;
import entity.Employeestatus;
import entity.Gender;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;
import static ui.Main.stage;
import static ui.MainWindowController.lastDirectory;


/**
 * FXML Controller class
 *
 * @author Suranga
 */
public class EmployeeUIController implements Initializable {
    //<editor-fold defaultstate="collapsed" desc="USER-data">
    //Color indication of Input Controls
    private final String valid = Style.valid;
    private final String invalid = Style.invalid;
    private final String updated = Style.updated;
    private final String initial = Style.initial;
    
    //Binding with the Form, Table
    private Employee employee;
    private ObservableList<Employee> employees;
    
    //Update Identification
    private Employee oldEmployee;
    
    //Table Row, Page Selected
    private int page;
    private int row;
    
    //Photo Selection
    private boolean photoSelected;
    
    //Search Lock
    private boolean lock;
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="FXML-DATA">
    
    
    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<Gender> cmbGender;
    @FXML
    private ComboBox<Civilstatus> cmbCivilstatus;
    @FXML
    private TextArea txaAddress;
    @FXML
    private DatePicker dtpDOB;
    @FXML
    private TextField txtNIC;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtLand;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<Designation> cmbDesignation;
    @FXML
    private DatePicker dtpAssign;
    @FXML
    private Button btnImage;
    @FXML
    private Button btnImageClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txtSearchName;
    @FXML
    private ComboBox<?> cmbSearchDesignation;
    @FXML
    private ComboBox<?> cmbSearchGender;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Button btnSearchLock;
    @FXML
    private ImageView imgEmployee;
    @FXML
    private StackPane stpPagination;
    @FXML
    private TableView<Employee> tblEmployee;
    @FXML
    private TableColumn  colName;
    @FXML
    private TableColumn  colGender;
    @FXML
    private TableColumn  colMobile;
    @FXML
    private TableColumn  colEmail;
    @FXML
    private TableColumn  colDesignation;
    @FXML
    private Pagination pagination;
    @FXML
    private Label lblDesiignationNew;
    @FXML
    private TextArea txaRecord;
//</editor-fold>
    private Object lastDirectory;
    

    /**
     * Initializes the controller class.
     */
    @Override
    //<editor-fold defaultstate="collapsed" desc="INITIALIZING- METHODS">
    
    
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        loadTable();
    }
    private void loadForm(){
        
        employee = new Employee();
        employee.setEmployeestatusId((Employeestatus)EmployeestatusDao.getAll().get(0));
        oldEmployee = null;
        photoSelected = false;
        
        
        cmbGender.setItems(GenderDao.getAll());
        cmbGender.getSelectionModel().select(-1);
        
        cmbSearchGender.setItems(GenderDao.getAll());
        cmbSearchGender.getSelectionModel().select(-1);
        
        cmbCivilstatus.setItems(CivilstatusDao.getAll());
        cmbCivilstatus.getSelectionModel().select(-1);
        
        cmbDesignation.setItems(DesignationDao.getAll());
        cmbDesignation.getSelectionModel().select(-1);
        
        cmbSearchDesignation.setItems(DesignationDao.getAll());
        cmbSearchDesignation.getSelectionModel().select(-1);
        
        txtName.setText("");
        txaAddress.setText("");
        txtNIC.setText("");
        txtMobile.setText("");
        txtLand.setText("");
        txtEmail.setText("");
        
        dtpDOB.setValue(null);
        dtpAssign.setValue(null);
        
        imgEmployee.setImage(new Image("/image/p1.png"));
        
        disableButtons(false,false,true,true);
        setStyle(initial);
        
    }
    
        private void loadTable() {

        lock = false;
        btnSearchLock.setText("Lock");

        cmbSearchGender.setItems(GenderDao.getAll());
        cmbSearchGender.getSelectionModel().select(-1);
        cmbSearchDesignation.setItems(DesignationDao.getAll());
        cmbSearchDesignation.getSelectionModel().select(-1);
        txtSearchName.setText("");

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colGender.setCellValueFactory(new PropertyValueFactory("genderId"));
        colMobile.setCellValueFactory(new PropertyValueFactory("mobile"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDesignation.setCellValueFactory(new PropertyValueFactory("designationId"));

        fillTable(EmployeeDao.getAll());
        pagination.setCurrentPageIndex(0);
        
    }
        
        private void fillTable(ObservableList<Employee> employees) {

        if (employees != null && employees.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((employees.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    loadForm();
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? employees.size() : pageIndex * rowsCount + rowsCount;
                    tblEmployee.getItems().clear();
                    tblEmployee.setItems(FXCollections.observableArrayList(employees.subList(start, end)));
                    return tblEmployee;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblEmployee.getItems().clear();
            loadForm();

        }

    }
        
        
    
    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete){
        
        btnAdd.setDisable(insert);
        btnUpdate.setDisable(update);
        btnDelete.setDisable(delete);
        
    }
    
    private void setStyle(String style) {
        
        cmbGender.setStyle(style);
        cmbCivilstatus.setStyle(style);
        cmbDesignation.setStyle(style);
        
        txtName.setStyle(style);
        txtNIC.setStyle(style);
        txtMobile.setStyle(style);
        txtLand.setStyle(style);
        txtEmail.setStyle(style);
        
        if (!txaAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }
        
        dtpDOB.getEditor().setStyle(style);
        dtpAssign.getEditor().setStyle(style);
        
    }
    
    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Form-Data Setters">
    @FXML
    private void txtNameKR(KeyEvent event) {
        if (employee.setName(txtName.getText().trim())&& EmployeeDao.getByName(txtName.getText().trim()) == null  ) {
            if (oldEmployee != null && !employee.getName().equals(oldEmployee.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            if (oldEmployee != null && employee.getName() != null && employee.getName().equals(oldEmployee.getName())) {
                txtName.setStyle(valid);
            } else {
                txtName.setStyle(invalid);
            }
        }
    }
    
    @FXML
    private void cmbGenderAP(ActionEvent event) {
        employee.setGenderId(cmbGender.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getGenderId().equals(oldEmployee.getGenderId())) {
            cmbGender.setStyle(updated);
        } else {
            cmbGender.setStyle(valid);
        }
    }
    
    @FXML
    private void cmbCivilstatusAP(ActionEvent event) {
        employee.setCivilstatusId(cmbCivilstatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
            cmbCivilstatus.setStyle(updated);
        } else {
            cmbCivilstatus.setStyle(valid);
        }
        
    }
    
    @FXML
    private void txaAddressKR(KeyEvent event) {
        if (employee.setAddress(txaAddress.getText().trim())) {
            if (oldEmployee != null && !employee.getAddress().equals(oldEmployee.getAddress())) {
                ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }
    
    @FXML
    private void dtpDobAP(ActionEvent event) {
        if (dtpDOB.getValue() != null && employee.setDob(java.sql.Date.valueOf(dtpDOB.getValue()))) {
            if (oldEmployee != null && !employee.getDob().equals(oldEmployee.getDob())) {
                dtpDOB.getEditor().setStyle(updated);
            } else {
                dtpDOB.getEditor().setStyle(valid);
            }
        } else {
            employee.setDob(null);
            dtpDOB.getEditor().setStyle(invalid);
        }
    }
    
    @FXML
    private void txtNicKR(KeyEvent event) {
         if (employee.setNic(txtNIC.getText().trim()) && EmployeeDao.getByNic(txtNIC.getText().trim()) == null) {
            if (oldEmployee != null && !employee.getNic().equals(oldEmployee.getNic())) {
                txtNIC.setStyle(updated);
            } else {
                txtNIC.setStyle(valid);
            }
        } else {
            if (oldEmployee != null && employee.getNic() != null && employee.getNic().equals(oldEmployee.getNic())) {
                txtNIC.setStyle(valid);
            } else {
                txtNIC.setStyle(invalid);
            }
        }
        
    }
    
    @FXML
    private void txtMobileKR(KeyEvent event) {
       if (employee.setMobile(txtMobile.getText().trim())) {
            if (oldEmployee != null && !employee.getMobile().equals(oldEmployee.getMobile())) {
                txtMobile.setStyle(updated);
            } else {
                txtMobile.setStyle(valid);
            }
        } else {
            txtMobile.setStyle(invalid);
        }
    }
    
    @FXML
    private void txtLandKR(KeyEvent event) {
        if (employee.setLand(txtLand.getText())) {
            if (oldEmployee != null && oldEmployee.getLand() != null && employee.getLand() != null && oldEmployee.getLand().equals(employee.getLand())) {
                txtLand.setStyle(valid);
            } else {
                if (oldEmployee != null && oldEmployee.getLand() != employee.getLand()) {
                    txtLand.setStyle(updated);
                } else {
                    txtLand.setStyle(valid);
                }
            }
        } else {
            txtLand.setStyle(invalid);
        }
    }
    
    @FXML
    private void txtEmailKR(KeyEvent event) {
        if (employee.setEmail(txtEmail.getText())) {
            if (oldEmployee != null && oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail())) {
                txtEmail.setStyle(valid);
            } else {
                if (oldEmployee != null && oldEmployee.getEmail() != employee.getEmail()) {
                    txtEmail.setStyle(updated);
                } else {
                    txtEmail.setStyle(valid);
                }
            }
        } else {
            txtEmail.setStyle(invalid);
        }
    }
    
    @FXML
    private void cmbDesignationAP(ActionEvent event) {
        employee.setDesignationId(cmbDesignation.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
            cmbDesignation.setStyle(updated);
        } else {
            cmbDesignation.setStyle(valid);
        }
        
    }
    
    @FXML
    private void dtpAssignAP(ActionEvent event) {
        if (dtpAssign.getValue() != null && employee.setAssign(java.sql.Date.valueOf(dtpAssign.getValue()))) {
            if (oldEmployee != null && !employee.getAssign().equals(oldEmployee.getAssign())) {
                dtpAssign.getEditor().setStyle(updated);
            } else {
                dtpAssign.getEditor().setStyle(valid);
            }
        } else {
            employee.setAssign(null);
            dtpAssign.getEditor().setStyle(invalid);
        }
    }
    
    @FXML
    private void btnImageKP(KeyEvent event) {
    }
    
    
    @FXML
    private void btnImageAP(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory((File) lastDirectory);
        }
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(stage);
        lastDirectory = file.getParentFile();
        
        if (file != null) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] image = new byte[(int) file.length()];
                DataInputStream dataIs = new DataInputStream(new FileInputStream(file));
                dataIs.readFully(image);
                
                ImageIcon img = new ImageIcon(image);
                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.'));
                if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".gif")) {
                    
                    if (img.getIconHeight() <= 100 && img.getIconWidth() <= 100) {
                        Image photo = new Image(fis);
                        imgEmployee.setImage(photo);
                        employee.setPhoto(image);
                        photoSelected = true;
                        
                    } else {
                        
                        Dialogs.create().title("Error Message").masthead("Photo Select").message("The Image Size should smaller than 100x100 Pixel").showError();
                        photoSelected = false;
                    }
                } else {
                    Dialogs.create().title("Error Message").masthead("Photo Select").message("The File should be JPG, JPEG, GIF or PNG").showError();
                    photoSelected = false;
                }
                
            } catch (FileNotFoundException ex) {
                // Logger.getLogger(EmployeeUIController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error");
                
            } catch (IOException ex) {
                //Logger.getLogger(EmployeeUIController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error");
            }
            
        } else {
            photoSelected = false;
        }
        
    }
    
    
    @FXML
    private void btnImageClearKP(KeyEvent event) {
        
    }
    
    @FXML
    private void btnImageClearAP(ActionEvent event) {
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Form- Operations">
   public String getErrors() {

        String errors = "";

        if (employee.getName() == null) {
            errors = errors + "Name \t\tis Invalid\n";
        }
        if (employee.getGenderId() == null) {
            errors = errors + "Gender \t\tis Not Selected\n";
        }
        if (employee.getCivilstatusId() == null) {
            errors = errors + "Civilstatus \tis Not Selected\n";
        }
        if (employee.getAddress() == null) {
            errors = errors + "Address \t\tis Invalid\n";
        }

        if (employee.getDob() == null) {
            errors = errors + "Birth Date \tis Invalid\n";
        }
        if (employee.getNic() == null) {
            errors = errors + "NIC No. \t\tis Invalid\n";
        }
        if (employee.getMobile() == null) {
            errors = errors + "Mobile No. \tis Invalid\n";
        }

        if (txtLand.getText() != null && !employee.setLand(txtLand.getText().trim())) {
            errors = errors + "Land No. \t\tis Invalid\n";
        }
        if (txtEmail.getText() != null && !employee.setEmail(txtEmail.getText().trim())) {
            errors = errors + "Email \t\tis Invalid\n";
        }

        if (employee.getDesignationId() == null) {
            errors = errors + "Designation \tis Not Selected\n";
        }

        if (employee.getAssign() == null) {
            errors = errors + "Assign Date \tis Invalid\n";
        }

        return errors;

    }
   public String getUpdates() { 

        String updates = "";

        if (oldEmployee != null && getErrors().isEmpty()) {

            if (!employee.getName().equals(oldEmployee.getName())) {
                updates = updates + oldEmployee.getName() + " chnaged to " + employee.getName() + "\n";
            }

            if (!employee.getAddress().equals(oldEmployee.getAddress())) {
                updates = updates + oldEmployee.getAddress() + " chnaged to " + employee.getAddress() + "\n";
            }

            if (!employee.getNic().equals(oldEmployee.getNic())) {
                updates = updates + oldEmployee.getNic() + " chnaged to " + employee.getNic() + "\n";
            }

            if (!(oldEmployee.getLand() != null
                    && employee.getLand() != null
                    && oldEmployee.getLand().equals(employee.getLand()))) {
                if (oldEmployee.getLand() != employee.getLand()) {
                    updates = updates + oldEmployee.getLand()
                            + " chnaged to " + employee.getLand() + "\n";
                }
            }

            if (!(oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail()))) {
                if (oldEmployee.getEmail() != employee.getEmail()) {
                    updates = updates + oldEmployee.getEmail() + " chnaged to " + employee.getEmail() + "\n";
                }
            }

            if (!employee.getMobile().equals(oldEmployee.getMobile())) {
                updates = updates + oldEmployee.getMobile() + " chnaged to " + employee.getMobile() + "\n";
            }

            if (!employee.getGenderId().equals(oldEmployee.getGenderId())) {
                updates = updates + oldEmployee.getGenderId() + " chnaged to " + employee.getGenderId() + "\n";
            }

            if (!employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
                updates = updates + oldEmployee.getCivilstatusId() + " chnaged to " + employee.getCivilstatusId() + "\n";
            }
            if (!employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
                updates = updates + oldEmployee.getDesignationId() + " chnaged to " + employee.getDesignationId() + "\n";
            }

            if (!employee.getDob().equals(oldEmployee.getDob())) {
                updates = updates + oldEmployee.getDob().toString() + "(dob)" + " chnaged to " + employee.getDob().toString() + "\n";
            }

            if (!employee.getAssign().equals(oldEmployee.getAssign())) {
                updates = updates + oldEmployee.getAssign().toString() + " chnaged to " + employee.getAssign().toString() + "\n";
            }

            if (photoSelected) {
                updates = updates + "Photo Changed\n";
            }

        }

        return updates;

    }
    
    @FXML
    private void btnAddKP(KeyEvent event) {
    }
    
    @FXML
    private void btnAddAP(ActionEvent event) {
        
        String errors = getErrors();
        
        if (errors.isEmpty()){
            
            String confermation = "Are you sure you need to add this Employee with following details\n "
                    + "\nName :         \t\t" + employee.getName()
                    + "\nGender :       \t\t" + employee.getGenderId().getName()
                    + "\nCivilstatus :  \t\t" + employee.getCivilstatusId().getName()
                    + "\nAddress :      \t\t" + employee.getAddress()
                    + "\nBirth Date :   \t\t" + employee.getDob().toString()
                    + "\nNIC No :       \t\t" + employee.getNic()
                    + "\nMobile No :    \t\t" + employee.getMobile()
                    + "\nLand :         \t\t" + employee.getLand()
                    + "\nEmail :        \t\t" + employee.getEmail()
                    + "\nDesignation :  \t\t" + employee.getDesignationId().getName()
                    + "\nAssigned Date :  \t" + employee.getAssign().toString()
                    + "\nPhoto :  \t\t\t" + (employee.getPhoto() == null ? "null" : "Selected");
            
            Action action = Dialogs.create().title("Confirm Message").masthead("Employee Add").message(confermation).showConfirm();
             
            if (action.toString().equals("DialogAction.YES")){
                try{                    
                   EmployeeDao.add(employee);
                Notifications.create().title("Successs").text("Employee " + employee.getName() + " saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                txaRecord.appendText(LocalTime.now() + " Employee " + employee.getName() + " Inserted.\n");
                
                loadTable();
                pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                tblEmployee.getSelectionModel().select(tblEmployee.getItems().size() - 1);
            }
                catch(DaoException ex){
                     Notifications.create().title("Un_Successs").text("Employee " + employee.getName() + " Not saved.\n Try Again.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                     txaRecord.appendText(LocalTime.now() + ex.getMessage());
                }
           }      
        }
        else{
            Dialogs.create().title("Error Message").masthead("Employee Detail Error").message(errors).showError();
        }
    }
    
    @FXML
    private void btnClearKP(KeyEvent event) {
    }
    
    @FXML
    private void btnClearAP(ActionEvent event) {
        
        Action action = Dialogs.create().styleClass("dlg").title("Confirm Message").masthead("Clear Form").message("Are you sure you want to clear the Form?").showConfirm();
            
        if(action.toString().equals("DialogAction.YES")){
        loadForm();
    }
    }
    @FXML
    private void btnUpdateKP(KeyEvent event) {
    }
    
    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();
        if (errors.isEmpty()) {
            String updates = getUpdates();
            if (!updates.isEmpty()) {

                Action action = Dialogs.create().title("Confirm Message").masthead("Employee Update").message(updates).showConfirm();
                if (action.toString().equals("DialogAction.YES")) {                  

                    
                }
            } else {
                Dialogs.create().title("Information Message").masthead("Employee Update").message("Nothing Updated").showWarning();
            }
        } else {

            Dialogs.create().title("Error Message").masthead("Employee Update").message(getErrors()).showError();

        }
    }
    
    @FXML
    private void btnDeleteKP(KeyEvent event) {
    }
    
    @FXML
    private void btnDeleteAP(ActionEvent event) {
    }
    
    private void fillForm(Employee selectedEmployee) {

        disableButtons(false, true, false, false);
        setStyle(valid);

        oldEmployee = EmployeeDao.getById(selectedEmployee.getId());
        employee = EmployeeDao.getById(selectedEmployee.getId());

        cmbGender.getSelectionModel().select((Gender) employee.getGenderId());
        cmbCivilstatus.getSelectionModel().select((Civilstatus) employee.getCivilstatusId());
        cmbDesignation.getSelectionModel().select((Designation) employee.getDesignationId());

        txtName.setText(employee.getName());
        txaAddress.setText(employee.getAddress());
        txtNIC.setText(employee.getNic());
        txtMobile.setText(employee.getMobile());
        txtLand.setText(employee.getLand());
        txtEmail.setText(employee.getEmail());

        dtpDOB.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob())));
        dtpDOB.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob()));
        dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssign())));
        dtpAssign.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssign()));

        if (employee.getPhoto() != null) {
            imgEmployee.setImage(new Image(new ByteArrayInputStream(employee.getPhoto())));
        } else {
            imgEmployee.setImage(new Image("/image/p1.png"));
        }

        page = pagination.getCurrentPageIndex();
        row = tblEmployee.getSelectionModel().getSelectedIndex();

    }
//</editor-fold>

    @FXML
    private void txtSearchNameKR(KeyEvent event) {
    }

    @FXML
    private void cmbSearchDesignationAP(ActionEvent event) {
    }

    @FXML
    private void cmbSearchGenderAP(ActionEvent event) {
    }

    @FXML
    private void btnSearchClearKP(KeyEvent event) {
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
    }

    @FXML
    private void btnSearchLockKP(KeyEvent event) {
    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {
    }

    @FXML
    private void tblEmployeeMC(MouseEvent event) {
        
        fillForm((Employee) tblEmployee.getSelectionModel ().getSelectedItem());
        
    }

    @FXML
    private void tblEmployeeKR(KeyEvent event) {
    }

    @FXML
    private void lblDesignationNewMC(MouseEvent event) {
    }
    
}
