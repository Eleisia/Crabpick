package com.e107.backend.geChu.domain.entity;

import com.e107.backend.geChu.security.entity.Authority;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //////////////////////////////////park
    //!!steamid(번호 고유 id) 사용
    @Column(name = "username", length = 50, unique = true)
    private String username;

    //!!primaryclanid 사용
    @Column(name = "password", length = 100)
    private String password;

    //!! displayname
    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
    //////////////////////////////////

//    @org.hibernate.annotations.Comment("유저 이름")
//    private String name;

//    @Email
//    @org.hibernate.annotations.Comment("유저 이메일")
//    private String email;

//    @org.hibernate.annotations.Comment("스팀 토큰")
//    private String steamToken;

//    @org.hibernate.annotations.Comment("스팀 닉네임")
//    private String steamNickname;

    @org.hibernate.annotations.Comment("스팀 친구목록")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Friend> friends = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SteamLibrary> steamLibraries = new ArrayList<>();
}


