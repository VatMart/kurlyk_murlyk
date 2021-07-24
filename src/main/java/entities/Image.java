package entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "images")
public class Image {

    public Image() {
    }

    public Image(String image) {
        this.image = image;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "img_id")
    private UUID img_id;
    @Column(name = "image")
    private String image;

    public UUID getImg_id() {
        return img_id;
    }

    public void setImg_id(UUID img_id) {
        this.img_id = img_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

