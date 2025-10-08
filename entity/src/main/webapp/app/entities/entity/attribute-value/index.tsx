import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AttributeValue from './attribute-value';
import AttributeValueDetail from './attribute-value-detail';
import AttributeValueUpdate from './attribute-value-update';
import AttributeValueDeleteDialog from './attribute-value-delete-dialog';

const AttributeValueRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AttributeValue />} />
    <Route path="new" element={<AttributeValueUpdate />} />
    <Route path=":id">
      <Route index element={<AttributeValueDetail />} />
      <Route path="edit" element={<AttributeValueUpdate />} />
      <Route path="delete" element={<AttributeValueDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AttributeValueRoutes;
