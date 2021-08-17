package com.escotech.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] img300;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] img600;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] img1600;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] scaled100;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] scaled600;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] scaled1600;

    public Image(String name, byte[] img300, byte[] img600, byte[] img1600, byte[] scaled100, byte[] scaled600, byte[] scaled1600) {

        this.name = name;
        this.img300 = img300;
        this.img600 = img600;
        this.img1600 = img1600;
        this.scaled100 = scaled100;
        this.scaled600 = scaled600;
        this.scaled1600 = scaled1600;
    }
}
