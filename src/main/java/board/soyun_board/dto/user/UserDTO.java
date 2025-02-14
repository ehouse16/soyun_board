package board.soyun_board.dto.user;

import board.soyun_board.entity.user.Role;
import board.soyun_board.entity.user.User;
import lombok.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private String email;
    private String password;
    private String name;
    private LocalTime joinDate;
    private LocalTime modifiedDate;
    private Role role;

    public Map<String, Object> getDataMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        map.put("name", name);
        map.put("role", role);
        return map;
    }

    public UserDTO(User user){
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.joinDate = user.getCreatedDate();
        this.modifiedDate = user.getLastModifiedDate();
        this.role = user.getRole();
    }
}
