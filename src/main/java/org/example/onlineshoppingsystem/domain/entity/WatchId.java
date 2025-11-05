package org.example.onlineshoppingsystem.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class WatchId implements Serializable {

    private Long user;
    private Long product;

    @Override
    public boolean equals(Object o){

        if (this == o) {
            return true;
        }

        if (!(o instanceof WatchId w)) {
            return false;
        }

        return Objects.equals(user, w.user) && Objects.equals(product, w.product);
    }

    @Override
    public int hashCode(){
        return Objects.hash(user, product);
    }
}
