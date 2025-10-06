import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface LocaleState {
  currentLocale: string;
  translationSourcePrefix?: string;
}

const initialState: LocaleState = {
  currentLocale: 'en',
  translationSourcePrefix: '',
};

const localeSlice = createSlice({
  name: 'locale',
  initialState,
  reducers: {
    setLocale(state, action: PayloadAction<string>) {
      state.currentLocale = action.payload;
    },
    addTranslationSourcePrefix(state, action: PayloadAction<string>) {
      // thêm prefix (ví dụ: "entity.", "menu.", v.v.)
      state.translationSourcePrefix = action.payload;
    },
  },
});

export const { setLocale, addTranslationSourcePrefix } = localeSlice.actions;

export default localeSlice.reducer;
