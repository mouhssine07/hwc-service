import { create } from "zustand";

const STORAGE_KEY = "hwc_client_auth";

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

const useClientAuthStore = create((set) => ({
  clientUser: storedAuth?.clientUser ?? null,
  clientToken: storedAuth?.clientToken ?? null,
  isClientAuthenticated: Boolean(storedAuth?.clientToken),

  clientLogin: (clientToken, clientUser) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ clientToken, clientUser }));
    set({ clientToken, clientUser, isClientAuthenticated: true });
  },

  clientLogout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ clientToken: null, clientUser: null, isClientAuthenticated: false });
  },

  loadClientFromStorage: () => {
    const auth = readStoredAuth();
    set({
      clientToken: auth?.clientToken ?? null,
      clientUser: auth?.clientUser ?? null,
      isClientAuthenticated: Boolean(auth?.clientToken),
    });
  },
}));

export default useClientAuthStore;
