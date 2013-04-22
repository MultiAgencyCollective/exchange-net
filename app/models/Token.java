package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Token extends Model {

    public String text;

    public Token() {
        // do nothing
    }
}
