import { Navigate } from "react-router-dom";
import { getCurrentUser } from "../utils/storage";

export default function AdminRoute({ children }) {
  const user = getCurrentUser();
  const isAdmin = user?.role === "SUPER_ADMIN";
  return isAdmin ? children : <Navigate to="/" replace />;
}
