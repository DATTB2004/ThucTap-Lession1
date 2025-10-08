import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Variant from './variant';
import VariantDetail from './variant-detail';
import VariantUpdate from './variant-update';
import VariantDeleteDialog from './variant-delete-dialog';

const VariantRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Variant />} />
    <Route path="new" element={<VariantUpdate />} />
    <Route path=":id">
      <Route index element={<VariantDetail />} />
      <Route path="edit" element={<VariantUpdate />} />
      <Route path="delete" element={<VariantDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VariantRoutes;
