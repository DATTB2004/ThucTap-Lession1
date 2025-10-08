import { IAttribute } from 'app/shared/model/entity/attribute.model';
import { IVariant } from 'app/shared/model/entity/variant.model';

export interface IAttributeValue {
  id?: number;
  value?: string;
  attribute?: IAttribute | null;
  variant?: IVariant | null;
}

export const defaultValue: Readonly<IAttributeValue> = {};
