package com.osipov.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spinjar.com.fasterxml.jackson.annotation.JsonAlias;
import spinjar.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Warrior implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String title;
    @JsonAlias("number")
    private Integer hp;
    private Boolean isAlive;
}
