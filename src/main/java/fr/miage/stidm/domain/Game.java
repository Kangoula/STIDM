package fr.miage.stidm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Game.
 */

@Document(collection = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("ref")
    private String ref;

    @Field("name")
    private String name;

    @Field("type")
    private String type;

    @Field("release")
    private LocalDate release;

    @Field("description")
    private String description;

    @Field("cover")
    private String cover;

    @Field("video")
    private String video;

    @Field("key_ref")
    private String keyRef;

    @DecimalMin(value = "0")
    @Field("price")
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public Game ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Game type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getRelease() {
        return release;
    }

    public Game release(LocalDate release) {
        this.release = release;
        return this;
    }

    public void setRelease(LocalDate release) {
        this.release = release;
    }

    public String getDescription() {
        return description;
    }

    public Game description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public Game cover(String cover) {
        this.cover = cover;
        return this;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideo() {
        return video;
    }

    public Game video(String video) {
        this.video = video;
        return this;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getKeyRef() {
        return keyRef;
    }

    public Game keyRef(String keyRef) {
        this.keyRef = keyRef;
        return this;
    }

    public void setKeyRef(String keyRef) {
        this.keyRef = keyRef;
    }

    public Double getPrice() {
        return price;
    }

    public Game price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if (game.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", ref='" + ref + "'" +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", release='" + release + "'" +
            ", description='" + description + "'" +
            ", cover='" + cover + "'" +
            ", video='" + video + "'" +
            ", keyRef='" + keyRef + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
