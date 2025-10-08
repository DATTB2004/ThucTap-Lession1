import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAttributes } from 'app/entities/entity/attribute/attribute.reducer';
import { getEntities as getVariants } from 'app/entities/entity/variant/variant.reducer';
import { createEntity, getEntity, reset, updateEntity } from './attribute-value.reducer';

export const AttributeValueUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const attributes = useAppSelector(state => state.entity.attribute.entities);
  const variants = useAppSelector(state => state.entity.variant.entities);
  const attributeValueEntity = useAppSelector(state => state.entity.attributeValue.entity);
  const loading = useAppSelector(state => state.entity.attributeValue.loading);
  const updating = useAppSelector(state => state.entity.attributeValue.updating);
  const updateSuccess = useAppSelector(state => state.entity.attributeValue.updateSuccess);

  const handleClose = () => {
    navigate('/entity/attribute-value');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAttributes({}));
    dispatch(getVariants({}));
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

    const entity = {
      ...attributeValueEntity,
      ...values,
      attribute: attributes.find(it => it.id.toString() === values.attribute?.toString()),
      variant: variants.find(it => it.id.toString() === values.variant?.toString()),
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
          ...attributeValueEntity,
          attribute: attributeValueEntity?.attribute?.id,
          variant: attributeValueEntity?.variant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="entityApp.entityAttributeValue.home.createOrEditLabel" data-cy="AttributeValueCreateUpdateHeading">
            Create or edit a Attribute Value
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="attribute-value-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Value"
                id="attribute-value-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="attribute-value-attribute" name="attribute" data-cy="attribute" label="Attribute" type="select">
                <option value="" key="0" />
                {attributes
                  ? attributes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="attribute-value-variant" name="variant" data-cy="variant" label="Variant" type="select">
                <option value="" key="0" />
                {variants
                  ? variants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/entity/attribute-value" replace color="info">
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

export default AttributeValueUpdate;
