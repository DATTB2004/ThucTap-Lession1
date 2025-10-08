import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './variant.reducer';

export const VariantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const variantEntity = useAppSelector(state => state.entity.variant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="variantDetailsHeading">Variant</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{variantEntity.id}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{variantEntity.price}</dd>
          <dt>
            <span id="stock">Stock</span>
          </dt>
          <dd>{variantEntity.stock}</dd>
          <dt>Product</dt>
          <dd>{variantEntity.product ? variantEntity.product.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/entity/variant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entity/variant/${variantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VariantDetail;
