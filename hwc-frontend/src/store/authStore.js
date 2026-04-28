import { create } from "zustand";

const STORAGE_KEY = "hwc_admin_auth";

function readStoredAuth() {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    return stored ? JSON.parse(stored) : null;
  } catch {
    localStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

const storedAuth = readStoredAuth();

const useAuthStore = create((set) => ({
  user: storedAuth?.user ?? null,
  token: storedAuth?.token ?? null,
  isAuthenticated: Boolean(storedAuth?.token),

  login: (token, user) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ token, user }));
    set({ token, user, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ token: null, user: null, isAuthenticated: false });
  },

  loadFromStorage: () => {
    const auth = readStoredAuth();
    set({
      token: auth?.token ?? null,
      user: auth?.user ?? null,
      isAuthenticated: Boolean(auth?.token),
    });
  },
}));

export default useAuthStore;
