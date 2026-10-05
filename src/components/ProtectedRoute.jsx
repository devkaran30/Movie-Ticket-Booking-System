import { Navigate, useLocation } from "react-router-dom";
import { getCurrentUser } from "../utils/storage";

export default function ProtectedRoute({ children }) {
  const location = useLocation();
  const user = getCurrentUser();
  return user ? children : <Navigate to="/login" replace state={{ from: location }} />;
}
