package models;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by wongk523 on 3/3/16.
 */
@Entity
public class UserModel extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String email;  // you can add annotations like @Required, @ManyToMany etc

    @Constraints.Required
    public String password;  // you can add annotations like @Required, @ManyToMany etc

    @Constraints.Required
    public String fullname;  // you can add annotations like @Required, @ManyToMany etc

    @Constraints.Required
    public boolean isadmin;  // you can add annotations like @Required, @ManyToMany etc

    public static void create(UserModel m){
        m.save();
    }

    public static Finder<Long, UserModel> find = new Finder<>(UserModel.class);
}
