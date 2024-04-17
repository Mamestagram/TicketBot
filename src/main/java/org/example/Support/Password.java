package org.example.Support;

import org.example.Main;
import org.example.Object.Database;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

//パスワードの変更を行うクラス
public abstract class Password {

public static boolean changeUserPassword(String user, String verificationCode, String password) throws SQLException, NoSuchAlgorithmException {

        Database d = Main.database;

        Connection connection = d.getConnection(d.getDB_HOST(), d.getDB_NAME(), d.getDB_USER(), d.getDB_PASSWORD());
        PreparedStatement ps;
        ResultSet result;

        ps = connection.prepareStatement("select verification_code from users where name = ?");
        ps.setString(1, user);
        result = ps.executeQuery();

        if(result.next()) {

            if(Objects.equals(verificationCode, result.getString("verification_code"))) {

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] rs = md5.digest(password.getBytes());
                int[] i = new int[rs.length];
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < rs.length; j++) {
                    i[j] = (int) rs[j] & 0xff;
                    if (i[j] <= 15) {
                        sb.append("0");
                    }
                    sb.append(Integer.toHexString(i[j]));
                }
                String hash = sb.toString();
                BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
                String encodeedPassword = bcpe.encode(hash);
                ps = connection.prepareStatement("update users set pw_bcrypt = ? where name = ?");
                ps.setString(1, encodeedPassword);
                ps.setString(2, user);
                ps.executeUpdate();

                connection.close();

                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
