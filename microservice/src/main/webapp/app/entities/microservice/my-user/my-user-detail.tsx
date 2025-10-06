import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-user.reducer';

export const MyUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myUserEntity = useAppSelector(state => state.microservice.myUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myUserDetailsHeading">My User</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{myUserEntity.id}</dd>
          <dt>
            <span id="idUser">Id User</span>
          </dt>
          <dd>{myUserEntity.idUser}</dd>
          <dt>
            <span id="userName">User Name</span>
          </dt>
          <dd>{myUserEntity.userName}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{myUserEntity.password}</dd>
        </dl>
        <Button tag={Link} to="/microservice/my-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/microservice/my-user/${myUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyUserDetail;
