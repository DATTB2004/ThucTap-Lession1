export interface IMyUser {
  id?: number;
  idUser?: number | null;
  userName?: string | null;
  password?: string | null;
}

export const defaultValue: Readonly<IMyUser> = {};
