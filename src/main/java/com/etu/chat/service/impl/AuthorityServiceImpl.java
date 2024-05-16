package com.etu.chat.service.impl;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.Room;
import com.etu.chat.repository.AuthorityRepository;
import com.etu.chat.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository repository;

    private String getAuthorityName(AuthorityPattern pattern, long id) {
        return "CAN_%s_%d".formatted(pattern.name().toUpperCase(), id);
    }

    @Override
    public Authority getAccessAuthorityRoom(AuthorityPattern pattern, Room room) {
        return repository.findByName(getAuthorityName(pattern, room.getId())).orElseThrow();
    }

    @Override
    public void createAuthorityForRoom(Room room) {
        repository.save(Authority.builder().
                name(getAuthorityName(AuthorityPattern.READ_ROOM, room.getId()))
                .build());
        repository.save(Authority.builder().
                name(getAuthorityName(AuthorityPattern.WRITE_ROOM, room.getId()))
                .build());
    }

    @Override
    public void deleteAuthorityForRoom(Room room) {
        repository.deleteByName(getAuthorityName(AuthorityPattern.READ_ROOM, room.getId()));
        repository.deleteByName(getAuthorityName(AuthorityPattern.WRITE_ROOM, room.getId()));
    }
}
