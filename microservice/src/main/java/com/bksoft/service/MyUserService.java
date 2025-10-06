package com.bksoft.service;

import com.bksoft.domain.MyUser;
import com.bksoft.repository.MyUserRepository;
import com.bksoft.service.dto.MyUserDTO;
import com.bksoft.service.mapper.MyUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bksoft.domain.MyUser}.
 */
@Service
@Transactional
public class MyUserService {

    private static final Logger LOG = LoggerFactory.getLogger(MyUserService.class);

    private final MyUserRepository myUserRepository;

    private final MyUserMapper myUserMapper;

    public MyUserService(MyUserRepository myUserRepository, MyUserMapper myUserMapper) {
        this.myUserRepository = myUserRepository;
        this.myUserMapper = myUserMapper;
    }

    /**
     * Save a myUser.
     *
     * @param myUserDTO the entity to save.
     * @return the persisted entity.
     */
    public MyUserDTO save(MyUserDTO myUserDTO) {
        LOG.debug("Request to save MyUser : {}", myUserDTO);
        MyUser myUser = myUserMapper.toEntity(myUserDTO);
        myUser = myUserRepository.save(myUser);
        return myUserMapper.toDto(myUser);
    }

    /**
     * Update a myUser.
     *
     * @param myUserDTO the entity to save.
     * @return the persisted entity.
     */
    public MyUserDTO update(MyUserDTO myUserDTO) {
        LOG.debug("Request to update MyUser : {}", myUserDTO);
        MyUser myUser = myUserMapper.toEntity(myUserDTO);
        myUser = myUserRepository.save(myUser);
        return myUserMapper.toDto(myUser);
    }

    /**
     * Partially update a myUser.
     *
     * @param myUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MyUserDTO> partialUpdate(MyUserDTO myUserDTO) {
        LOG.debug("Request to partially update MyUser : {}", myUserDTO);

        return myUserRepository
            .findById(myUserDTO.getId())
            .map(existingMyUser -> {
                myUserMapper.partialUpdate(existingMyUser, myUserDTO);

                return existingMyUser;
            })
            .map(myUserRepository::save)
            .map(myUserMapper::toDto);
    }

    /**
     * Get all the myUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MyUserDTO> findAll() {
        LOG.debug("Request to get all MyUsers");
        return myUserRepository.findAll().stream().map(myUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one myUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MyUserDTO> findOne(Long id) {
        LOG.debug("Request to get MyUser : {}", id);
        return myUserRepository.findById(id).map(myUserMapper::toDto);
    }

    /**
     * Delete the myUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MyUser : {}", id);
        myUserRepository.deleteById(id);
    }
}
