import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignIn extends JDialog{
    private JPasswordField passwordField1;
    private JButton signInButton;
    private JButton signUpButton;
    private JTextField textFieldEmail;
    private JPanel loginPanel;



    public SignIn(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(1440,1024));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textFieldEmail.getText();
                String password = String.valueOf(passwordField1.getPassword());

                user = getAuthenticatedUser(email, password);

                if(user != null){
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(SignIn.this, "Email or Password Invalid","Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);

    }

    public User user;
    private User getAuthenticatedUser(String email, String password){
        User user = null;

        final String url = "jdbc:mysql://localhost:3306/JobListingDatabase";
        final String usernameToDatabase = "root";
        final String passwordToDatabase = "1723";

        try{
            Connection conn = DriverManager.getConnection(url, usernameToDatabase, passwordToDatabase);

            Statement stmt = conn.createStatement();
            String sql = "select * from Candidates where Email=? AND Password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User();
                user.CandidateID = resultSet.getInt("CandidateID");
                user.FirstName = resultSet.getString("FirstName");
                user.LastName = resultSet.getString("LastName");
                user.Email = resultSet.getString("Email");
                user.Password = resultSet.getString("Password");
                user.Phone = resultSet.getString("Phone");
                user.Desired_Salary = resultSet.getInt("Desired_Salary");
                user.Location = resultSet.getString("Location");
            }

            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        SignIn signIn = new SignIn(null);
        User user = signIn.user;

        if(user != null){
            System.out.println("Successfull authentication of: " + user.FirstName + " " + user.LastName);
        }
        else{
            System.out.println("Authentication cancelled");
        }


    }
}

