import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  headers: { "Content-Type": "application/json" },
  timeout: 10000,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message ||
      error.response?.data?.error ||
      (typeof error.response?.data === "string" ? error.response.data : null) ||
      error.message ||
      "Request failed";
    return Promise.reject(new Error(message));
  }
);

export const authApi = {
  login: (email, password) =>
    api.post("/auth/login", null, { params: { email, password } }),
  register: (user) => api.post("/auth/register", user),
};

export const movieApi = {
  list: () => api.get("/movies"),
  get: (id) => api.get(`/movies/${id}`),
  create: (movie) => api.post("/movies", movie),
  update: (id, movie) => api.put(`/movies/${id}`, movie),
  remove: (id) => api.delete(`/movies/${id}`),
};

export const theatreApi = {
  list: () => api.get("/theatres"),
  get: (id) => api.get(`/theatres/${id}`),
  create: (value) => api.post("/theatres", value),
  update: (id, value) => api.put(`/theatres/${id}`, value),
  remove: (id) => api.delete(`/theatres/${id}`),
};

export const screenApi = {
  list: () => api.get("/screens"),
  get: (id) => api.get(`/screens/${id}`),
  create: (value) => api.post("/screens", value),
  update: (id, value) => api.put(`/screens/${id}`, value),
  remove: (id) => api.delete(`/screens/${id}`),
};

export const showApi = {
  list: () => api.get("/shows"),
  get: (id) => api.get(`/shows/${id}`),
  seats: (id) => api.get(`/shows/${id}/seats`),
  create: (value) => api.post("/shows", value),
  update: (id, value) => api.put(`/shows/${id}`, value),
  remove: (id) => api.delete(`/shows/${id}`),
};

export const seatApi = {
  list: () => api.get("/seats"),
  get: (id) => api.get(`/seats/${id}`),
  create: (value) => api.post("/seats", value),
  update: (id, value) => api.put(`/seats/${id}`, value),
  remove: (id) => api.delete(`/seats/${id}`),
};

export const seatLockApi = {
  create: (value) => api.post("/seat-locks", value),
  list: () => api.get("/seat-locks"),
  remove: (id) => api.delete(`/seat-locks/${id}`),
};

export const bookingApi = {
  create: (value) => api.post("/bookings/create", value),
  list: () => api.get("/bookings"),
  listByUser: (userId) => api.get(`/bookings/user/${userId}`),
  get: (id) => api.get(`/bookings/${id}`),
  cancel: (id) => api.post(`/bookings/${id}/cancel`),
};

export const paymentApi = {
  process: (value) => api.post("/payments/process", value),
  list: () => api.get("/payments"),
};

export const ticketApi = {
  list: () => api.get("/tickets"),
  listByUser: (userId) => api.get(`/tickets/user/${userId}`),
};

export const userApi = {
  list: () => api.get("/users"),
  get: (id) => api.get(`/users/${id}`),
  update: (id, value) => api.put(`/users/${id}`, value),
};

export const adminApi = {
  movie: {
    create: (value) => api.post("/admin/movies", value),
    update: (id, value) => api.put(`/admin/movies/${id}`, value),
    remove: (id) => api.delete(`/admin/movies/${id}`),
  },
  show: {
    create: (value) => api.post("/admin/shows", value),
    update: (id, value) => api.put(`/admin/shows/${id}`, value),
    remove: (id) => api.delete(`/admin/shows/${id}`),
  },
  theatre: {
    create: (value) => api.post("/admin/theatres", value),
    update: (id, value) => api.put(`/admin/theatres/${id}`, value),
    remove: (id) => api.delete(`/admin/theatres/${id}`),
  },
  screen: {
    create: (value) => api.post("/admin/screens", value),
    update: (id, value) => api.put(`/admin/screens/${id}`, value),
    remove: (id) => api.delete(`/admin/screens/${id}`),
  },
  seat: {
    create: (value) => api.post("/admin/seats", value),
    update: (id, value) => api.put(`/admin/seats/${id}`, value),
    updateStatus: (id, status) => api.put(`/admin/seats/${id}/status`, { status }),
    remove: (id) => api.delete(`/admin/seats/${id}`),
  },
  user: {
    updateStatus: (id, status) => api.put(`/admin/users/${id}/status`, { status }),
    remove: (id) => api.delete(`/admin/users/${id}`),
  },
};

export default api;
