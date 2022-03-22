package presentation;

import bussiness.DeliveryService;
import data.Role;
import data.Serializer;
import data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class UserGUI extends JFrame implements Serializable {
    private List<User> users;

    private DeliveryService deliveryService;

    private AdminGUI adminGUI;
    private ClientGUI clientGUI;
    private EmployeeGUI employeeGUI;

    private JLabel dataLabel = new JLabel("Enter your data");

    private JLabel userNameLabel = new JLabel("User Name: ");
    private JLabel passwordLabel = new JLabel("Password: ");

    private JTextField userNameText = new JTextField(20);
    private JTextField passwordText = new JTextField(20);

    private JButton logInButton = new JButton("LOG IN");
    private JButton signInButton = new JButton("SIGN UP");

    public UserGUI(DeliveryService deliveryService) throws IOException {
        this.deliveryService = deliveryService;

        employeeGUI = new EmployeeGUI(deliveryService);
        defineWindowListener();
        users = deliveryService.getUsers();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel[] contents = setPanels(6);

        dataLabel.setFont(new Font(dataLabel.getFont().getName(), Font.BOLD, 16));
        contents[0].add(dataLabel);

        contents[1].add(userNameLabel);
        contents[1].add(userNameText);

        contents[2].add(passwordLabel);
        contents[2].add(passwordText);

        final JComboBox roles = new JComboBox(Role.values());
        contents[3].add(roles);

        logInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contents[4].add(logInButton);
        contents[5].add(signInButton);

        for(int i = 0; i < 6; i++){
            content.add(contents[i]);
        }

        //buttons
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameText.getText();
                String password  = passwordText.getText();
                if(userName.isEmpty() || password.isEmpty()){
                    showError("Enter valid data!");
                    return;
                }
                boolean exists = false;
                Role role = null;
                int userIndex = 0;
                for(User u : users) {
                    if (u.getUserName().compareTo(userName) == 0) {
                        if (u.getPassword().compareTo(password) == 0) {
                            role = u.getRole();
                            userIndex = users.indexOf(u) + 1;
                            break;
                        }
                        else{
                            showError("Username and password don't match!");
                            exists = true;
                        }
                    }
                }
                if(role == null && !exists){
                    showError("User doesn't exist!");
                }
                else if(role != null){

                    switch(role){
                        case ADMINISTRATOR:
                            adminGUI = new AdminGUI(deliveryService);
                            break;
                        case CLIENT:
                            clientGUI = new ClientGUI(employeeGUI, userIndex);
                            break;
                        default:
                            employeeGUI.setVisible(true);
                    }
                }
            }
        });

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameText.getText();
                String password  = passwordText.getText();
                if(userName.isEmpty() || password.isEmpty()){
                    showError("Enter valid data!");
                    return;
                }
                String roleString = String.valueOf(roles.getSelectedItem());
                Role role = null;

                switch(roleString){
                    case "ADMINISTRATOR":  role = Role.ADMINISTRATOR;
                        break;
                    case "CLIENT": role = Role.CLIENT;
                        break;
                    default: role = Role.EMPLOYEE;
                }

                User newUser = new User(userName, password, role);
                boolean success = true;
                for(User u : users){
                    if(u.getUserName().compareTo(newUser.getUserName()) == 0){
                        showError("Already existing userName!");
                        success = false;
                        break;
                    }
                }

                if(success){
                    users.add(newUser);
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 300);
        this.setTitle("Food Delivery Management");
        this.setLayout(new BorderLayout());

        this.setContentPane(content);
    }
    private JPanel[] setPanels(int noPanels){
        JPanel [] contents = new JPanel[noPanels];
        for(int i = 0; i < noPanels; i++){
            contents[i] = new JPanel();
            contents[i].setLayout(new FlowLayout(FlowLayout.CENTER));
        }
        return contents;
    }
    private void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
    private void defineWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("INCHIDE");
                Serializer.serialize(deliveryService);
                super.windowClosing(e);
            }
        });
    }
}
