package com.smart.watchboard.domain;

import com.smart.watchboard.common.enums.Role;
import com.smart.watchboard.common.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

//    @OneToMany(mappedBy = "user")
//    private List<UserDocument> userDocuments = new ArrayList<>();

    public void authorizeUser() {
        this.role = Role.USER;
    }

}
