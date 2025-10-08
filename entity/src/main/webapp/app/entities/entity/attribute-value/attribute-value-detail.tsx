import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './attribute-value.reducer';

export const AttributeValueDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const attributeValueEntity = useAppSelector(state => state.entity.attributeValue.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="attributeValueDetailsHeading">Attribute Value</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{attributeValueEntity.id}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{attributeValueEntity.value}</dd>
          <dt>Attribute</dt>
          <dd>{attributeValueEntity.attribute ? attributeValueEntity.attribute.id : ''}</dd>
          <dt>Variant</dt>
          <dd>{attributeValueEntity.variant ? attributeValueEntity.variant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/entity/attribute-value" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entity/attribute-value/${attributeValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AttributeValueDetail;
