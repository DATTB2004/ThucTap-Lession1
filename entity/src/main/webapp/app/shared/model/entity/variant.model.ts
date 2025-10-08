import { IProduct } from 'app/shared/model/entity/product.model';

export interface IVariant {
  id?: number;
  price?: number | null;
  stock?: number | null;
  product?: IProduct | null;
}

export const defaultValue: Readonly<IVariant> = {};
