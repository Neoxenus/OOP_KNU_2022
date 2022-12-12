package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    String name;
    int age;
    int height;
}
