import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { authApi } from "../services/api";
import { saveCurrentUser } from "../utils/storage";

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true); setError("");
    try {
      const response = await authApi.login(form.email.trim(), form.password);
      saveCurrentUser(response.data);
      navigate(location.state?.from?.pathname || (response.data.role === "SUPER_ADMIN" ? "/admin" : "/"), { replace: true });
    } catch (err) { setError(err.message); } finally { setLoading(false); }
  };

  return <AuthLayout title="Welcome back" subtitle="Sign in to continue booking.">
    {error && <div className="alert error">{error}</div>}
    <form onSubmit={submit} className="auth-form">
      <label>Email<input className="input" type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></label>
      <label>Password<input className="input" type="password" required value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></label>
      <button className="btn btn-primary full" disabled={loading}>{loading ? "Signing in…" : "Sign in"}</button>
    </form>
    <p className="auth-footer">New here? <Link to="/register">Create an account</Link></p>
  </AuthLayout>;
}

function AuthLayout({ title, subtitle, children }) {
  return <section className="auth-page"><div className="auth-card"><span className="eyebrow">MOVIEBOOK</span><h1>{title}</h1><p>{subtitle}</p>{children}</div></section>;
}
