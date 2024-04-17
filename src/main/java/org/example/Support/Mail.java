package org.example.Support;

import org.example.Main;
import org.example.Object.Database;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public abstract class Mail {

    //returnでerrを返した時、無効なリクエストとなる
    private static String createRandomVerificationCode(String user) {

        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        PreparedStatement ps;
        ResultSet result;

        Random random = new Random();

        try {
            Database d = Main.database;
            int verification_code = random.nextInt(100001, 999999);
            String hash = bcpe.encode(String.valueOf(verification_code));
            Connection connection = d.getConnection(d.getDB_HOST(), d.getDB_NAME(), d.getDB_USER(), d.getDB_PASSWORD());

            ps = connection.prepareStatement("select id from users where name = ?");
            ps.setString(1, user);
            result = ps.executeQuery();

            if(result.next()) {
                ps = connection.prepareStatement("update users set verification_code = ? where id = ?");
                ps.setString(1, hash);
                ps.setInt(2, result.getInt("id"));
                ps.executeUpdate();
                return hash;
            }

            return "err";
        } catch (SQLException e) {
            e.printStackTrace();
            return "err";
        }
    }

    public static boolean sendVerificationMail(String email, String user) throws IOException, FileNotFoundException {

        Properties prop = new Properties();
        /*
        二段階認証が必要なので控えておく (mail.propertiesも設置する！)

        * mailaddress= 自分のメールアドレス
          password= アプリパスワード
          mail.smtp.auth=true
          mail.smtp.starttls.enable=true
          mail.smtp.host=smtp.gmail.com
          mail.smtp.port=587
        * */
        prop.load(new FileReader("mail.properties"));

        final String mail = prop.getProperty("mailaddress");
        final String password = prop.getProperty("password");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        try {

            String verificationCode = createRandomVerificationCode(user);

            if(Objects.equals(verificationCode, "err")) return false;

            Message message = new MimeMessage(session);
            //受信元
            message.setFrom(new InternetAddress(mail));
            //送信先
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(prop.getProperty(mail)));
            //件名
            message.setSubject("Account Verification");
            //内容
            message.setText("Hello! " + user + ".\nYour verification code is " + verificationCode + ".\n\nThank you for playing on Mamestagram!");

            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            System.out.println("Email sent unsuccessfully : " + e);
            return false;
        }
    }
}
