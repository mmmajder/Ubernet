package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    //    @Lob
    private byte[] data;
    private boolean isActive;
    @ManyToOne
    private User user;

    public Image(User user, byte[] data) {
        this.user = user;
//        this.data = data;
        this.isActive = true;
    }
}
