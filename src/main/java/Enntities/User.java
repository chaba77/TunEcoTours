package Enntities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.image.Image;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.scene.image.ImageView;
import com.google.zxing.WriterException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class User {
    private Image image;
    private ImageView imageView;
    private int id;
    private String email;
    private String password;
    private List<String> roles;
    private int is_verified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public User(String email, String password, List<String> roles, int is_verified) throws IOException, WriterException {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.is_verified = is_verified;
        image = generateQRCodeImage("hello");
        this.imageView = new ImageView(this.image);
        imageView.setFitHeight(50); // Adjust size as needed
        imageView.setFitWidth(50);
    }
    public User() {
    }

    private Image generateQRCodeImage(String text) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return new Image(new ByteArrayInputStream(pngData));
    }
    public ImageView getImageView() {
        return imageView;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", is_verified=" + is_verified +
                '}';
    }

    public User(int id, String email, String password, List<String> roles, int is_verified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.is_verified = is_verified;
    }

}
