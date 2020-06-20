package br.com.test.domain;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "address")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Please provide a street name")
    @Column(name = "street_name")
    private String streetName;
    @NotNull(message = "Please provide a number")
    private Integer number;
    private String complement;
    @NotEmpty(message = "Please provide a neighbourhood")
    private String neighbourhood;
    @NotEmpty(message = "Please provide a city")
    private String city;
    @NotEmpty(message = "Please provide a state")
    private String state;
    @NotEmpty(message = "Please provide a country")
    private String country;
    @NotEmpty(message = "Please provide a zip code")
    @Column(name = "zip_code")
    private String zipCode;
    private String latitude;
    private String longitude;
}