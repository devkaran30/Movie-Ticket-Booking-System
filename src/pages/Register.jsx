import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authApi } from "../services/api";
import { saveCurrentUser } from "../utils/storage";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", email: "", phoneNumber: "", password: "", role: "USER", status: "ACTIVE" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const update = (field, value) => setForm((current) => ({ ...current, [field]: value }));

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true); setError("");
    try {
      const response = await authApi.register(form);
      saveCurrentUser(response.data);
      navigate("/");
    } catch (err) { setError(err.message); } finally { setLoading(false); }
  };

  return <section className="auth-page"><div className="auth-card wide"><span className="eyebrow">MOVIEBOOK</span><h1>Create your account</h1><p>Register as a customer to book movie tickets.</p>{error && <div className="alert error">{error}</div>}
    <form onSubmit={submit} className="auth-form two-col">
      <label>Name<input className="input" required value={form.name} onChange={(e) => update("name", e.target.value)} /></label>
      <label>Phone number<input className="input" value={form.phoneNumber} onChange={(e) => update("phoneNumber", e.target.value)} /></label>
      <label className="span-2">Email<input className="input" type="email" required value={form.email} onChange={(e) => update("email", e.target.value)} /></label>
      <label className="span-2">Password<input className="input" type="password" required minLength="4" value={form.password} onChange={(e) => update("password", e.target.value)} /></label>
      <button className="btn btn-primary full span-2" disabled={loading}>{loading ? "Creating…" : "Create account"}</button>
    </form>
    <p className="auth-footer">Already registered? <Link to="/login">Sign in</Link></p>
  </div></section>;
}
