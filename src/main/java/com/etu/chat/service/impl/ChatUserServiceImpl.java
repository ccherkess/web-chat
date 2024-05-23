package com.etu.chat.service.impl;

import com.etu.chat.dto.UserWithAuthorities;
import com.etu.chat.entity.Authority;
import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;
import com.etu.chat.repository.ChatUserRepository;
import com.etu.chat.service.AuthorityService;
import com.etu.chat.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("chatUserService")
@RequiredArgsConstructor
class ChatUserServiceImpl implements ChatUserService {

    private static final Authority ADMIN_AUTHORITY = Authority.builder().name("ROLE_ADMIN").build();

    private final ChatUserRepository repository;
    private final AuthorityService authorityService;

    @Override
    public Optional<ChatUser> find(String username) {
        return repository.findByName(username);
    }

    @Override
    @Transactional
    public void addAuthorityForUser(String username, Authority authority) {
        find(username).ifPresentOrElse(
                user -> {
                    user.getAuthorities().add(authority);
                    repository.save(user);
                },
                () -> {
                    throw new RuntimeException();
                }
        );
    }

    @Override
    @Transactional
    public void removeAuthorityForUser(String username, Authority authority) {
        find(username).ifPresentOrElse(
                user -> {
                    user.getAuthorities().remove(authority);
                    repository.save(user);
                },
                () -> {
                    throw new RuntimeException();
                }
        );
    }

    @Override
    @Transactional
    public boolean hasAuthority(String username, Authority authority) {
        return repository.findByName(username)
                .flatMap(user -> user.getAuthorities().stream()
                        .filter(authority::equals)
                        .findAny())
                .isPresent();
    }

    @Override
    @Transactional
    public boolean isCanReadRoom(String username, Room room) {
        Authority forReadRoom = authorityService.getAccessAuthorityRoom(AuthorityService.AuthorityPattern.READ_ROOM, room);

        return hasAuthority(username, forReadRoom) || hasAuthority(username, ADMIN_AUTHORITY);
    }

    @Override
    public void allowReadRoom(Room room, String username, boolean isAllow) {
        find(username).ifPresentOrElse(
                user -> {
                    Authority authority = authorityService.getAccessAuthorityRoom(AuthorityService.AuthorityPattern.READ_ROOM, room);
                    if (isAllow) {
                        addAuthorityForUser(username, authority);
                    } else {
                        removeAuthorityForUser(username, authority);
                    }
                },
                () -> {
                    throw new RuntimeException();
                }
        );
    }

    @Override
    @Transactional
    public boolean isCanWriteRoom(String username, Room room) {
        Authority forWriteRoom = authorityService.getAccessAuthorityRoom(AuthorityService.AuthorityPattern.WRITE_ROOM, room);

        return hasAuthority(username, forWriteRoom) || hasAuthority(username, ADMIN_AUTHORITY);
    }

    @Override
    public void allowWriteRoom(Room room, String username, boolean isAllow) {
        find(username).ifPresentOrElse(
                user -> {
                    Authority authority = authorityService.getAccessAuthorityRoom(AuthorityService.AuthorityPattern.WRITE_ROOM, room);
                    if (isAllow) {
                        addAuthorityForUser(username, authority);
                    } else {
                        removeAuthorityForUser(username, authority);
                    }
                },
                () -> {
                    throw new RuntimeException();
                }
        );
    }

    @Override
    public UserWithAuthorities serializeUserWithAuthorities(ChatUser user, Room room) {

        return new UserWithAuthorities(user.getName(), isCanReadRoom(user.getName(), room), isCanWriteRoom(user.getName(), room));
    }
}
