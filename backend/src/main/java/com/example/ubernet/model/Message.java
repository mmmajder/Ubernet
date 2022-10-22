package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Message {
    @Id
    @Column(unique = true)
    private long id;

    @ManyToOne
    private User sender;
    private LocalDateTime time;
    private String content;

    private Boolean deleted = false;

}
