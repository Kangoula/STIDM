package fr.miage.stidm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CartElement.
 */

@Document(collection = "cart_element")
public class CartElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("id_game")
    private String idGame;

    @Field("ref_game")
    private String refGame;

    @Field("name_game")
    private String nameGame;

    @DecimalMin(value = "0")
    @Field("price")
    private Double price;

    @Field("id_user")
    private String idUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGame() {
        return idGame;
    }

    public CartElement idGame(String idGame) {
        this.idGame = idGame;
        return this;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public String getRefGame() {
        return refGame;
    }

    public CartElement refGame(String refGame) {
        this.refGame = refGame;
        return this;
    }

    public void setRefGame(String refGame) {
        this.refGame = refGame;
    }

    public String getNameGame() {
        return nameGame;
    }

    public CartElement nameGame(String nameGame) {
        this.nameGame = nameGame;
        return this;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public Double getPrice() {
        return price;
    }

    public CartElement price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIdUser() {
        return idUser;
    }

    public CartElement idUser(String idUser) {
        this.idUser = idUser;
        return this;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartElement cartElement = (CartElement) o;
        if (cartElement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cartElement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CartElement{" +
            "id=" + id +
            ", idGame='" + idGame + "'" +
            ", refGame='" + refGame + "'" +
            ", nameGame='" + nameGame + "'" +
            ", price='" + price + "'" +
            ", idUser='" + idUser + "'" +
            '}';
    }
}
