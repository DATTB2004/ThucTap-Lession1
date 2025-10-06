package com.bksoft.web.rest;

import com.bksoft.repository.MyUserRepository;
import com.bksoft.service.MyUserService;
import com.bksoft.service.dto.MyUserDTO;
import com.bksoft.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bksoft.domain.MyUser}.
 */
@RestController
@RequestMapping("/api/my-users")
public class MyUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(MyUserResource.class);

    private static final String ENTITY_NAME = "microserviceMyUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyUserService myUserService;

    private final MyUserRepository myUserRepository;

    public MyUserResource(MyUserService myUserService, MyUserRepository myUserRepository) {
        this.myUserService = myUserService;
        this.myUserRepository = myUserRepository;
    }

    /**
     * {@code POST  /my-users} : Create a new myUser.
     *
     * @param myUserDTO the myUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myUserDTO, or with status {@code 400 (Bad Request)} if the myUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyUserDTO> createMyUser(@RequestBody MyUserDTO myUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save MyUser : {}", myUserDTO);
        if (myUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new myUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        myUserDTO = myUserService.save(myUserDTO);
        return ResponseEntity.created(new URI("/api/my-users/" + myUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, myUserDTO.getId().toString()))
            .body(myUserDTO);
    }

    /**
     * {@code PUT  /my-users/:id} : Updates an existing myUser.
     *
     * @param id the id of the myUserDTO to save.
     * @param myUserDTO the myUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUserDTO,
     * or with status {@code 400 (Bad Request)} if the myUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyUserDTO> updateMyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyUserDTO myUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MyUser : {}, {}", id, myUserDTO);
        if (myUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        myUserDTO = myUserService.update(myUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, myUserDTO.getId().toString()))
            .body(myUserDTO);
    }

    /**
     * {@code PATCH  /my-users/:id} : Partial updates given fields of an existing myUser, field will ignore if it is null
     *
     * @param id the id of the myUserDTO to save.
     * @param myUserDTO the myUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUserDTO,
     * or with status {@code 400 (Bad Request)} if the myUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the myUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the myUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyUserDTO> partialUpdateMyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyUserDTO myUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MyUser partially : {}, {}", id, myUserDTO);
        if (myUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyUserDTO> result = myUserService.partialUpdate(myUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, myUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /my-users} : get all the myUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myUsers in body.
     */
    @GetMapping("")
    public List<MyUserDTO> getAllMyUsers() {
        LOG.debug("REST request to get all MyUsers");
        return myUserService.findAll();
    }

    /**
     * {@code GET  /my-users/:id} : get the "id" myUser.
     *
     * @param id the id of the myUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyUserDTO> getMyUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MyUser : {}", id);
        Optional<MyUserDTO> myUserDTO = myUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myUserDTO);
    }

    /**
     * {@code DELETE  /my-users/:id} : delete the "id" myUser.
     *
     * @param id the id of the myUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MyUser : {}", id);
        myUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
