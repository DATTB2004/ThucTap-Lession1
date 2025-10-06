import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './my-user.reducer';

export const MyUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const myUserEntity = useAppSelector(state => state.microservice.myUser.entity);
  const loading = useAppSelector(state => state.microservice.myUser.loading);
  const updating = useAppSelector(state => state.microservice.myUser.updating);
  const updateSuccess = useAppSelector(state => state.microservice.myUser.updateSuccess);

  const handleClose = () => {
    navigate('/microservice/my-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.idUser !== undefined && typeof values.idUser !== 'number') {
      values.idUser = Number(values.idUser);
    }

    const entity = {
      ...myUserEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...myUserEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="microserviceApp.microserviceMyUser.home.createOrEditLabel" data-cy="MyUserCreateUpdateHeading">
            Create or edit a My User
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="my-user-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Id User" id="my-user-idUser" name="idUser" data-cy="idUser" type="text" />
              <ValidatedField label="User Name" id="my-user-userName" name="userName" data-cy="userName" type="text" />
              <ValidatedField label="Password" id="my-user-password" name="password" data-cy="password" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/microservice/my-user" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MyUserUpdate;
