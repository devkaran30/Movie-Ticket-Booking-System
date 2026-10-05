import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { userApi } from "../services/api";
import { clearCurrentUser, getCurrentUser, saveCurrentUser } from "../utils/storage";

export default function Profile() {
  const navigate = useNavigate();
  const user = getCurrentUser();
  const [form, setForm] = useState({ name: user?.name || "", email: user?.email || "", phoneNumber: user?.phoneNumber || "" });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const save = async (event) => {
    event.preventDefault(); setError(""); setMessage("");
    try {
      const response = await userApi.update(user.id, { ...user, ...form });
      saveCurrentUser(response.data);
      setMessage("Profile updated.");
    } catch (err) { setError(err.message); }
  };
  const logout = () => { clearCurrentUser(); navigate("/"); };

  return <section className="page-section narrow"><div className="page-title"><div><span className="eyebrow">ACCOUNT</span><h1>My profile</h1><p>Manage the customer details stored in the backend.</p></div></div>
    {error && <div className="alert error">{error}</div>}{message && <div className="alert success">{message}</div>}
    <form className="profile-card auth-form" onSubmit={save}>
      <div className="profile-avatar">{user?.name?.charAt(0)?.toUpperCase()}</div>
      <label>Name<input className="input" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} /></label>
      <label>Email<input className="input" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></label>
      <label>Phone<input className="input" value={form.phoneNumber} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} /></label>
      <div className="profile-actions"><button className="btn btn-primary">Save changes</button><button type="button" className="btn btn-ghost" onClick={logout}>Logout</button></div>
    </form>
  </section>;
}
